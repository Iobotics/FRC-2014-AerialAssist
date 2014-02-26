public class HoughTransform extends Thread{
  
  // The size of the neighbourhood in which to search for other local maxima 
  final int neighbourhoodSize = 4; 
 
  // How many discrete values of theta shall we check? 
  final int maxTheta = 180; 
 
  // Using maxTheta, work out the step 
  final double thetaStep = PI / maxTheta; 
 
  // the width and height of the image 
  protected int width, height; 
 
  // the hough array 
  protected int[][] houghArray; 
 
  // the coordinates of the centre of the image 
  protected float centerX, centerY; 
 
  // the height of the hough array 
  protected int houghHeight; 
 
  // double the hough height (allows for negative numbers) 
  protected int doubleHeight; 
 
  // the number of points that have been added 
  protected int numPoints; 
 
  // cache of values of sin and cos for different theta values. Has a significant performance improvement. 
  private double[] sinCache; 
  private double[] cosCache; 
  
  int hWidth;
  int hHeight;
  
  public HoughTransform(int w, int h){
    hWidth = w;
    hHeight = h;
  
    initialise();
  }
  
  public void initialise(){
    //Calculate the maximum height the hough array needs to have
    houghHeght = int((sqrt(2)*max(hHeight, hWidth))/2);
    
    //Double the height of the hough array to cope with the negative r values
    doubleHeight = 2*houghHeight;
    
    //Create hough array
    houghArray = new int[maxTheta][doubleHeight];
    
    //Find edge points and vote in array
    centerX = hWidth/2;
    centerY = hHeight/2;
    
    //Count how many points there are
    numPoints;
    
    //Cache the values of sin and cos for faster processing
    sinCache = new double[maxTheta]; 
    cosCache = sinCache.clone(); 
    for (int t = 0; t < maxTheta; t++) { 
        double realTheta = t * thetaStep; 
        sinCache[t] = sin(realTheta); 
        cosCache[t] = cos(realTheta); 
    }
    
    
  }
  
  public void addPoints(PImage image){
    image.loadPixels();
    for(int x=0; x<image.width; x++){
      for(int y=0; y<image.width; y++){
        if(color(image.pixels[x+(y*image.width)])!=0){
          addPoint(x, y);
        }
      }
    }  
  }
  
  public void addPoint(int x, int y){
   // Go through each value of theta 
   for (int t = 0; t < maxTheta; t++) { 
 
     //Work out the r values for each theta step 
     int r = (int) (((x - centerX) * cosCache[t]) + ((y - centerY) * sinCache[t])); 
 
     // this copes with negative values of r 
     r += houghHeight; 
 
     if (r < 0 || r >= doubleHeight) continue; 
 
     // Increment the hough array 
     houghArray[t][r]++; 
   } 
  numPoints++;  
  }
  
  public PVector<HoughLine> getLines(int threshold){
    // Initialise the vector of lines that we'll return 
        Vector<HoughLine> lines = new Vector<HoughLine>(20); 
 
        // Only proceed if the hough array is not empty 
        if (numPoints == 0) return lines; 
 
        // Search for local peaks above threshold to draw 
        for (int t = 0; t < maxTheta; t++) { 
         loop: 
           for (int r = neighbourhoodSize; r < doubleHeight - neighbourhoodSize; r++) { 
 
           // Only consider points above threshold 
           if (houghArray[t][r] > threshold) { 
 
             int peak = houghArray[t][r]; 
 
             // Check that this peak is indeed the local maxima 
             for (int dx = -neighbourhoodSize; dx <= neighbourhoodSize; dx++) { 
             for (int dy = -neighbourhoodSize; dy <= neighbourhoodSize; dy++) { 
             int dt = t + dx; 
             int dr = r + dy; 
             if (dt < 0) dt = dt + maxTheta; 
             else if (dt >= maxTheta) dt = dt - maxTheta; 
             if (houghArray[dt][dr] > peak) { 
                // found a bigger point nearby, skip 
                continue loop; 
              } 
            } 
          } 
 
          // calculate the true value of theta 
          double theta = t * thetaStep; 
 
          // add the line to the vector 
          lines.add(new HoughLine(theta, r)); 
        } 
      } 
    } 
    return lines; 
  }
  
  /** 
     * Gets the highest value in the hough array 
     */ 
    public int getHighestValue() { 
        int max = 0; 
        for (int t = 0; t < maxTheta; t++) { 
            for (int r = 0; r < doubleHeight; r++) { 
                if (houghArray[t][r] > max) { 
                    max = houghArray[t][r]; 
                } 
            } 
        } 
        return max; 
    } 
}
