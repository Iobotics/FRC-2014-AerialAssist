import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import blobDetection.*;

private static final int _green_color_threshold = 110; //How far the color can be from green to be registered as a point
private static final int _blue_color_threshold = 120; //blue values need to be LESS than this in order to qualify a pixel as truly red
private static final int _red_color_threshold = 30; //red values need to be LESS than this

private static final double _rectangularityThreshold = 0.25;

private static final float aspect_ratio_horizontal = 6.0; //Ideal aspect ratios of the horizontal and vertical targets
private static final float aspect_ratio_vertical   = 0.125;
private static final float _aspectRatioTolerance = 0.25;
// Aspect ratios are validated within a 25% tolerance level (from 75 to 125 percent)

// Using AXIS 206 camera's lowest quality size to speed up image processing
private static int greenPixels;
private static final int camWidth = 320;
private static final int camHeight = 240;

private static final String _clientIPAddress = "10.99.99.2";
private static final String _cameraURL = "http://10.99.99.11/mjpg/video.mjpg";

int currentPixel; //The array index of the pixel we're looking at
PImage greenFiltered; //New black and white image based on the green pixels
IPCapture cam; //Frame from the camera

BlobDetection blobDetector = new BlobDetection(camWidth, camHeight);

NetworkTable table;

void setup() {
  println("Initializing...");
  NetworkTable.setClientMode();
  NetworkTable.setIPAddress(_clientIPAddress);
  table = NetworkTable.getTable("vision");
  greenFiltered = cam = new IPCapture(this, _cameraURL, "", "");
  cam.start();  
  size(camWidth, camHeight);
  blobDetector.setPosDiscrimination(false);
  blobDetector.setThreshold(0.5);
  blobDetector.setConstants(1000, 4000, 5000);
  println("Setup complete!");
}

boolean set = false;
boolean blobSet = false;

void draw(){  
  textSize(10);
  fill(50);
  if(cam.isAvailable()) {
    cam.read();   
    //image(greenFiltered,0,0);
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
          stroke(0, 0, 0);
          greenPixels++;
          greenFiltered.pixels[currentPixel] = color(0, 0, 0); //Map all green points to black on the new image
          point(x,y); //===FOR DEBUGGING: Puts a point at every point detected as green
        } else {
          greenFiltered.pixels[currentPixel] = color(255, 255, 255); //Map all nongreen points to white on the new image
          stroke(255, 255, 255);
          point(x,y);
        }
      }
    }
    
    greenFiltered.updatePixels();
    image(greenFiltered,0,0);
  
    //greenFiltered.
    blobDetector.computeBlobs(greenFiltered.pixels); //Compute blobs on the new image
    blobDetector.computeTriangles();
    
    stroke(255, 0, 0);
    noFill();
    
    int validBlobCount = 0;
    //Checks based on rectangularity and aspect ratio, each with 25% tolerance
    for(int i = 0; i < blobDetector.getBlobNb(); i++){
      Blob blob = blobDetector.getBlob(i);
      text("(" + i + "," + computeRectangularity(blob) + "," + computeAspectRatio(blob) + ", " + blob.getTriangleNb() + ", " + blob.getEdgeNb() +  ")", 0, 16 * (i + 1));
      point(denormalize(blob.x, width), denormalize(blob.y, height));
      rect(denormalize(blob.xMin, width), denormalize(blob.yMin, height), denormalize(blob.w, width), denormalize(blob.h, height));
      if(computeRectangularity(blobDetector.getBlob(i)) > _rectangularityThreshold && 
      ((computeAspectRatio(blobDetector.getBlob(i)) > aspect_ratio_horizontal * _lowToleranceFactor && //Horizontal target
      computeAspectRatio(blobDetector.getBlob(i)) < aspect_ratio_horizontal * _highToleranceFactor || 
      (computeAspectRatio(blobDetector.getBlob(i)) > aspect_ratio_vertical * _lowToleranceFactor &&  //Vertical target
      computeAspectRatio(blobDetector.getBlob(i)) < aspect_ratio_vertical * _highToleranceFactor)))){
        validBlobCount++;
      }
      if((computeRectangularity(blobDetector.getBlob(i)) > _rectangularityThreshold) && 
      (computeAspectRatio(blobDetector.getBlob(i)) > aspect_ratio_vertical * _lowToleranceFactor) && //Vertical target
      (computeAspectRatio(blobDetector.getBlob(i)) < aspect_ratio_vertical * _highToleranceFactor))  {
          if(blobDetector.getBlob(i).x > 0.5) {
            // blob is on right side of camera frame
            table.putBoolean("rightHot", true);
          }
          else  {
            // blob is on left side of camera frame
            table.putBoolean("rightHot", false);
          }
      }
    }
    //println("There are " + blobDetector.getBlobNb() + " blobs.");
    if(validBlobCount > 0)  {
      if(!blobSet && table.isConnected()) {
        table.putBoolean("blobDetected", true);
        blobSet = true;
        println("set 'blobDetected' to true; Valid blob cound == " + validBlobCount);
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
  greenFiltered.loadPixels();
  int pixelCount = 0;
  int left = round(denormalize(blob.xMin, width));
  int top = round(denormalize(blob.yMin, height));
  int bWidth = round(denormalize(blob.w, width));
  int bHeight = round(denormalize(blob.h, height));
  for(int x = 0; x < bWidth; x++){
    for(int y = 0; y < bHeight; y++){
      int imageX = left + x;
      int imageY = top + y;
      int currentPixel = imageX + (width * imageY);
      if(greenFiltered.pixels[currentPixel] == color(0, 0, 0)) pixelCount++; 
    }
  }
  return pixelCount;
}

String vertexString(EdgeVertex v) {
   return "(" + denormalize(v.x, width) + ", " + denormalize(v.y, height) + ")"; 
}

boolean checkAspectRatio(Blob blob, double threshold, double tolerance){
  double aspectRatio = computeAspectRatio(blob);
  return ((aspectRatio > (threshold * (1.0 - tolerance))) && (aspectRatio < (threshold * (1.0 + tolerance))));
}
boolean checkRectangularity(Blob blob, double threshold){
  return (computeRectangularity(blob) > threshold);
}

