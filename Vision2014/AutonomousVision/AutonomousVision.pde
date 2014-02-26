private static final int _color_threshold = 170; //How far the color can be from green to be registered as a point
private static final int _noncolor_threshold = 100; //blue and red values need to be LESS than this in order to qualify a pixel as truly red
private static final int _requiredPixelsPercentage = 70; //What percent of the examined pixels need to be green in order to register as true
//STUFF
private static int boundingLeft = 0;
private static int boundingRight = 256;
private static int boundingTop = 0;
private static int boundingBottom = 192;
// need method to autocalc and verify photo boundaries

int currentPixel; //The array index of the pixel we're looking at
PImage inputImage;

void setup(){
  inputImage = loadImage("test.jpg");
  size(inputImage.width, inputImage.height); //===FOR DEBUGGING
  background(255); //===FOR DEBUGGING
  inputImage.loadPixels();
  for(int x = boundingLeft; x < boundingRight; x++) { //Iterate through each column, beginning with boundingLeft and ending with boundingBottom
    for(int y = boundingTop; y < boundingBottom; y++){ //Iterate through each row, beginning with the boundingTop row and ending at boundingBottom 
      currentPixel = x + (y * inputImage.width);
      if((255 - green(inputImage.pixels[currentPixel]) < _color_threshold) 
           && (blue(inputImage.pixels[currentPixel]) < _noncolor_threshold) && 
           (red(inputImage.pixels[currentPixel]) < _noncolor_threshold)) { //Makes sure each pixel has a sufficient amount of green, and not too much red / blue
        point(x,y); //===FOR DEBUGGING: Puts a point at every point detected as green
      }
    }
  }
}