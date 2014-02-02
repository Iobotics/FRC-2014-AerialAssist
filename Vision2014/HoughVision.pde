//Apply filter first, then Hough transform
PImage inputImage = loadImage("test.jpg");
int currentPixel; //Array index of currently examined pixel
HoughTransform h = new HoughTransform(inputImage.width, inputImage.height);

void setup(){
//Main code starts here
filterImage(inputImage);
h.addPoints(inputImage);
// get the lines out 
PVector<HoughLine> lines = h.getLines(30); 
} 


void filterImage(PImage image){ //Turns pixel into all red pixels
  image.loadPixels();
  for(int x=0; x<image.width; x++){
    for(int y=0; y<image.height; y++){
      currentPixel=(x+(y*image.width));
      if((255-red(inputImage.pixels[currentPixel])<color_threshold)&&(blue(inputImage.pixels[currentPixel])<noncolor_threshold)&&(green(inputImage.pixels[currentPixel])<noncolor_threshold)){
        image.pixels[currentPixel]=color(0,0,0);
      } else{
        image.pixels[currentPixel]=color(255,255,255);
      }
    }
  }
}
