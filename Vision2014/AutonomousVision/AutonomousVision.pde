import edu.wpi.first.wpilibj.*;
import blobDetection.*;

private static final int _color_threshold = 170; //How far the color can be from green to be registered as a point
private static final int _noncolor_threshold = 150; //blue and red values need to be LESS than this in order to qualify a pixel as truly red

// Using AXIS 206 camera's lowest quality size to speed up image processing
private static final int camWidth = 320;
private static final int camHeight = 240;

int currentPixel; //The array index of the pixel we're looking at
int greenPixelCount; //The number of pixels that are registered as green
PImage inputImage;
PImage greenFiltered;
IPCapture cam;

void setup(){
  println("Initializing...");
  greenFiltered = cam = new IPCapture(this, "http://10.24.38.11/mjpg/video.mjpg", "", "");
  cam.start();  
  size(camWidth, camHeight);
}

void draw(){
  greenPixelCount = 0;
  
  if(cam.isAvailable()) {
    cam.read();   
    image(cam,0,0);
  }
  cam.loadPixels();
  greenFiltered.loadPixels();
  
  if(cam.pixels.length > 0){
    for(int x = 0; x < camWidth; x++) { //Iterate through each column
      for(int y = 0; y < camHeight; y++){ //Iterate through each row
        currentPixel = x + (y * cam.width);
        if((green(cam.pixels[currentPixel]) > _color_threshold) 
             && (blue(cam.pixels[currentPixel]) < _noncolor_threshold) 
             && (red(cam.pixels[currentPixel]) < _noncolor_threshold)) { //Makes sure each pixel has a sufficient amount of green, and not too much red / blue
          greenPixelCount++;
          stroke(255, 0, 0);
          greenFiltered.pixels[currentPixel] = color(0, 0, 0);
          point(x,y); //===FOR DEBUGGING: Puts a point at every point detected as green
        } else {
          greenFiltered.pixels[currentPixel] = color(255, 255, 255);
        }
      }
    }
  }
  greenFiltered.updatePixels();
}


