package elyazisitanima;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 

public class BarChart_AWT extends ApplicationFrame {
   
   public BarChart_AWT( String applicationTitle , String chartTitle,  int[] numFalseint , int[]numTrueClass) {
      super( applicationTitle );
      
      JFreeChart barChart = ChartFactory.createBarChart(
         chartTitle,           
         "Category",            
         "Score",            
         createDataset(numFalseint, numTrueClass ),          
         PlotOrientation.VERTICAL,           
         true, true, false);
         
      ChartPanel chartPanel = new ChartPanel( barChart );        
      chartPanel.setPreferredSize(new java.awt.Dimension( 1120 , 367 ) );        
      setContentPane( chartPanel ); 
   }
   
   private CategoryDataset createDataset( int[] numFalse, int[] numTrue) {
      final String fal = "FALSE";        
      final String tru = "TRUE";
      final String total ="TOTAL";
           
      final String[] numbers = new String[10]; 
      
      for(int i = 0 ; i < numbers.length;i++)
      {
    	  numbers[i] = ""+ i;
      }      
      
      final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );  

      for(int i = 0; i < numFalse.length; i++)
      {
      dataset.addValue( numFalse[i] , fal , numbers[i] );
      }
      
      for(int i = 0; i < numTrue.length; i++)
      {
      dataset.addValue( numTrue[i] , tru , numbers[i]);        
      }
      
      for(int i = 0; i < numTrue.length; i++)
      {
    	  int temp =numTrue[i]+numFalse[i];
    	  
      dataset.addValue( temp , total , numbers[i]);        
      }

               

      return dataset; 
   }
   
  
}