import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import blobDetection.*;

private static final int _green_color_threshold = 110; //How far the color can be from green to be registered as a point
private static final int _blue_color_threshold = 120; //blue values need to be LESS than this in order to qualify a pixel as truly red
private static final int _red_color_threshold = 30; //red values need to be LESS than this

// Using AXIS 206 camera's lowest quality size to speed up image processing
private static int greenPixels;
private static final int camWidth = 320;
private static final int camHeight = 240;
private static final double rectangularityThreshold = 0.75;

int currentPixel; //The array index of the pixel we're looking at
PImage greenFiltered; //New black and white image based on the green pixels
IPCapture cam; //Frame from the camera

BlobDetection blobDetector = new BlobDetection(camWidth, camHeight);

NetworkTable table;

void setup() {
  println("Initializing...");
  NetworkTable.setClientMode();
  NetworkTable.setIPAddress("10.24.38.2");
  table = NetworkTable.getTable("vision");
  greenFiltered = cam = new IPCapture(this, "http://10.24.38.11/mjpg/video.mjpg", "", "");
  cam.start();  
  size(camWidth, camHeight);
}

boolean set = false;
boolean blobSet = false;

void draw(){  
  if(cam.isAvailable()) {
    cam.read();   
    image(cam,0,0);
  }
  greenPixels = 0;
  cam.loadPixels();
  greenFiltered.loadPixels();
  
  if(cam.pixels.length > 0) {
    for(int x = 0; x < camWidth; x++) { //Iterate through each column
      for(int y = 0; y < camHeight; y++) { //Iterate through each row
        currentPixel = x + (y * cam.width);
        if((green(cam.pixels[currentPixel]) > _green_color_threshold) 
             && (blue(cam.pixels[currentPixel]) < _blue_color_threshold) 
             && (red(cam.pixels[currentPixel]) < _red_color_threshold)) { //Makes sure each pixel has a sufficient amount of green, and not too much red / blue
          stroke(255, 0, 0);
          greenPixels++;
          greenFiltered.pixels[currentPixel] = color(0, 0, 0); //Map all green points to black on the new image
          point(x,y); //===FOR DEBUGGING: Puts a point at every point detected as green
        } else {
          greenFiltered.pixels[currentPixel] = color(255, 255, 255); //Map all nongreen points to white on the new image
        }
      }
    }
    blobDetector.computeBlobs(greenFiltered.pixels); //Compute blobs on the new image
    Blob[] blobArray = new Blob[blobDetector.getBlobNb()];
    Scores[] scoresArray = new Scores[blobArray.length]; //Holds info about each blob
    for(int i = 0; i < blobArray.length; i++){
      blobArray[i] = blobDetector.getBlob(i);
    }
    if(greenPixels > 0)  {
      if(!blobSet && table.isConnected()) {
        table.putBoolean("blobDetected", true);
        blobSet = true;
        println("set 'blobDetected' to true; greenPixels == " + greenPixels);
      }
      else  {
        table.putBoolean("blobDetected", false);
        blobSet = false;
        println("reset blobDetected");
      }
    }
  }
  if(!set && table.isConnected()) {
    table.putBoolean("connected", true);
    set = true;
    println("set 'connected' to true");
  }
  greenFiltered.updatePixels();
}


double computeAspectRatio(Blob blob) {
  if(blob == null)  return 0.0;
  return denormalize(blob.w, camWidth) / denormalize(blob.h, camHeight);
}

float denormalize(double normalized, int scale) {
  return (float) normalized * scale;
}

double computeRectangularity(Blob blob) { //Returns proportional rectangularity; i.e. blob area / bounding box area
  if(blob == null)  return 0.0;
  float boundingBoxArea = denormalize(blob.w, camWidth) * denormalize(blob.h, camHeight);
  return blobArea(blob) / boundingBoxArea;
}
  

double blobArea(Blob blob) { //Returns the combined area of all triangles in a blob
  EdgeVertex point1; //Points that make up each triangle
  EdgeVertex point2;
  EdgeVertex point3;
  float length1; //Lengths of each triangle's edge
  float length2;
  float length3;
  float semiperimiter;
  double totalArea = 0;
  for(int i = 0; i < blob.getTriangleNb(); i++) { //Uses Hero's Formula to get the area of each triangle
    point1 = blob.getTriangleVertexA(blob.getTriangle(0));
    point2 = blob.getTriangleVertexB(blob.getTriangle(0));
    point3 = blob.getTriangleVertexA(blob.getTriangle(0));
    length1 = dist(denormalize(point1.x, camWidth), denormalize(point1.y, camHeight), denormalize(point2.x, camWidth), denormalize(point2.y, camHeight));
    length2 = dist(denormalize(point1.x, camWidth), denormalize(point1.y, camHeight), denormalize(point3.x, camWidth), denormalize(point3.y, camHeight));
    length3 = dist(denormalize(point2.x, camWidth), denormalize(point2.y, camHeight), denormalize(point3.x, camWidth), denormalize(point3.y, camHeight));
    semiperimiter = (length1 + length2 + length3) / 2;
    totalArea += sqrt(semiperimiter * (semiperimiter - length1) * (semiperimiter - length2) * (semiperimiter - length3));
  }
  return totalArea;
}
