import edu.wpi.first.wpilibj.*;

private static final int _color_threshold = 170; //How far the color can be from green to be registered as a point
private static final int _noncolor_threshold = 150; //blue and red values need to be LESS than this in order to qualify a pixel as truly red
private static final int _requiredPixelsPercentage = 70; //What percent of the examined pixels need to be green in order to register as true
//STUFF
private static int boundingLeft = 0;
private static int boundingRight;
private static int boundingTop = 0;
private static int boundingBottom;
private static int boundedArea;
// need method to autocalc and verify photo boundaries
private static int camWidth = 320;
private static int camHeight = 240;

int currentPixel; //The array index of the pixel we're looking at
int greenPixelCount = 0; //The number of pixels that are registered as green
PImage inputImage;
IPCapture cam;

void setup(){
  println("Initializing...");
  cam = new IPCapture(this, "http://10.24.38.11/mjpg/video.mjpg", "", "");
  cam.start();
  
  boundingRight = camWidth; //===FOR DEBUGGING: Looking at whole canvas instead of small portion (like we will later)
  boundingBottom = camHeight;
  boundedArea = (boundingRight - boundingLeft) * (boundingBottom - boundingTop); //Area of bounded region
  size(320, 240);
}

void draw(){
  if(cam.isAvailable()) {
    cam.read();   
    image(cam,0,0);
  }
  cam.loadPixels();
  if(cam.pixels.length > 0){
    for(int x = boundingLeft; x < boundingRight; x++) { //Iterate through each column, beginning with boundingLeft and ending with boundingBottom
      for(int y = boundingTop; y < boundingBottom; y++){ //Iterate through each row, beginning with the boundingTop row and ending at boundingBottom 
        currentPixel = x + (y * cam.width);
        if((green(cam.pixels[currentPixel]) > _color_threshold) 
             && (blue(cam.pixels[currentPixel]) < _noncolor_threshold) 
             && (red(cam.pixels[currentPixel]) < _noncolor_threshold)) { //Makes sure each pixel has a sufficient amount of green, and not too much red / blue
          greenPixelCount++;
          stroke(255, 0, 0);
          point(x,y); //===FOR DEBUGGING: Puts a point at every point detected as green
        }
      }
    }
  }
  /*
  println("Total green pixels: " + greenPixelCount); //===FOR DEBUGGING: Print how many green pixels there are
  println("Percentage of pixels that are green: " + ((greenPixelCount * 100) / boundedArea)); //===FOR DEBUGGING: Print what percentage of the pixels are green
  */
}
