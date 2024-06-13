import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HtmlParser
{
    private static double[][] data;
    public static void main(String[] args)
    {
        int mode = 0;
        int task = 0;
        String stock = "a";
        int start = 0;
        int end = 0;
        
        if (args.length >= 2)
        {
            try
            {
                mode = Integer.parseInt(args[0]);
                task = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e)
            {
                System.err.println("none");
            }
        }
        if (args.length >= 3)
        {
            stock = args[2];
        }
        if (args.length >= 4)
        {
            start = Integer.parseInt(args[3]);
            end = Integer.parseInt(args[4]);
        }
        if (mode == 0)
        {  
            crawlAndOutputData();
             
        }
        else if (mode == 1 && task == 0)
        {
            copyData();
        }
         else if (mode == 1 && task == 1)
        {
            mode1task1(stock,start,end);
        }
       
        else if (mode == 1 && task == 2)
        {
            mode1task2( stock , start , end);
        }
        else if (mode == 1 && task == 3) 
        {
            mode1task3(start, end);
        }
        else if (mode == 1 && task == 4)
        {
            mode1task4(stock, start, end);
        }
        else
        {
            System.out.println("you enter wron thing!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

    private static void crawlAndOutputData() 
    {
        try 
        {
            Document doc = Jsoup.connect("https://pd2-hw3.netdb.csie.ncku.edu.tw/").get();
            Elements rows = doc.select("tr");
            boolean isTitleDay30 = doc.title().equals("day30");
            File file = new File("data.csv");
            FileWriter writer = null;
            String title = doc.title();
            if (title.equals("day1")) 
            {
                writer = new FileWriter("data.csv", true);
                for (int i = 0; i < rows.size(); i++) 
                {
                    Element row = rows.get(i);
                    Elements rowData = row.select("th, td");
                    for (int j = 0; j < rowData.size(); j++) 
                    {
                        Element element = rowData.get(j);
                        if (element.tagName().equals("th")) 
                        {
                            if (j != rowData.size() - 1) 
                            {
                                writer.append(element.text() + ",");
                            } else 
                            {
                                writer.append(element.text()+"\n");
                            }
                        } 
                        else if (element.tagName().equals("td")) 
                        {
                            if (j != rowData.size() - 1) 
                            {
                                writer.append(element.text() + ",");
                            } 
                            else 
                            {
                                writer.append(element.text()+"\n");
                            }
                        }
                    }
                }
            } 
            else if(file.exists()&&!title.equals("day1"))
            {
                writer = new FileWriter("data.csv", true);
                for (int i = 0; i < rows.size(); i++) 
                {
                    Element row = rows.get(i);
                    Elements rowData = row.select("td");
                    for (int j = 0; j < rowData.size(); j++) 
                    {
                        Element element = rowData.get(j);
                        if (j != rowData.size() - 1) 
                        {
                            writer.append(element.text() + ",");
                        } else 
                        {
                            writer.append(element.text()+"\n");
                        }
                    }
                }
            }
            if (writer != null) 
            {
                writer.close();
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    private static void copyData() {
    try (BufferedReader reader = new BufferedReader(new FileReader("data.csv"));
         FileWriter writer = new FileWriter("output.csv", true)) { // 添加 true 参数表示追加模式
        String line;
        int count = 0;

        while ((line = reader.readLine()) != null && count < 31) {
            writer.append(line);
            writer.append("\n");
            count++;
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    public static void mode1task1(String searchString ,int startRow , int endRow)
    {
            copyData2();
            String csvFile = "data2.csv";
           
            String line;
            double[][] data2 = new double[1][endRow-startRow+1];
            double[][] data3 = new double[1][endRow-startRow+1];
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile)))
            {
                int lineCount = 1;
                String[] headers = null;
                int[][] place = new int[1][1];
               
                int dataLine = 0 ;
                while ((line = br.readLine()) != null && lineCount <= endRow+1)
                {
                    if (lineCount == 1)
       
                    {
                        headers = line.split(",");
                        for (int i = 0; i < headers.length; i++)
                        {
                            if (searchString.equals(headers[i]))
                            {
                                //System.out.println("find '" + searchString + "' on : " + (i + 1));
                                place[0][0] = i ;
                                break;
                            }
                        }
                    }
                    else if (lineCount >= startRow+1 && lineCount <= endRow+1)
                    {
                        String[] values = line.split(",");
                        data2[0][dataLine] = Double.parseDouble(values[place[0][0]]);
                        dataLine++ ;
                    }
                    lineCount++;
                }


            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                File file = new File("output.csv");
                boolean fileExists = file.exists();
                FileWriter writer = new FileWriter(fileExists ? "output.csv" : "output.csv", true);
                int count = 0 ;
                writer.append(searchString + "," + startRow + "," + endRow + "\n");
                double sum = 0.00;
                for(int i = 0 ; i < endRow - startRow + 1 ; i++)
                {
                    if(i < 4)
                    {
                        //System.out.print(data2[0][i]);
                        sum += data2[0][i];
                    }
                    else if(i >= 4 && i < endRow - startRow)
                    {
                        sum += data2[0][i];
                        double roundedNumber = roundToTwoDecimalPlaces(sum / 5);
                        data3[0][count] = roundedNumber ;
                        double value = data3[0][count];
                        String stringValue = (value == (int) value) ? String.valueOf((int) value) : String.valueOf(value);
                        writer.append(stringValue + ",");
                        sum = sum - data2[0][i - 4];
                    }
                    else if(i == endRow - startRow)
                    {
                        sum += data2[0][i];
                        double roundedNumber = roundToTwoDecimalPlaces(sum / 5);
                        data3[0][count] = roundedNumber ;
                        double value = data3[0][count];
                        String stringValue = (value == (int) value) ? String.valueOf((int) value) : String.valueOf(value);
                        writer.append(stringValue+"\n");
                    }
                }
                writer.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    }

    public static void mode1task2(String searchString ,int startRow , int endRow)
    {
        copyData2();
        String csvFile = "data2.csv";
        String line;
        double[][] data2 = new double[1][endRow-startRow+1];
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile)))
        {
            int lineCount = 1;
            String[] headers = null;
            int[][] place = new int[1][1];        
            int dataLine = 0 ;
            while ((line = br.readLine()) != null && lineCount <= endRow+1)
            {
                if(lineCount == 1)
                {
                    headers = line.split(",");
                    for (int i = 0; i < headers.length; i++)
                    {
                        if (searchString.equals(headers[i]))
                        {
                            place[0][0] = i ;
                            break;
                        }
                    }
                }
                else if (lineCount >= startRow+1 && lineCount <= endRow+1)
                {
                    String[] values = line.split(",");
                    data2[0][dataLine] = Double.parseDouble(values[place[0][0]]);
                    dataLine++ ;
                }
                lineCount++ ;
            }
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        try
        {
            File file = new File("output.csv");
            boolean fileExists = file.exists();
            FileWriter writer = new FileWriter(fileExists ? "output.csv" : "output.csv", true);
            writer.append(searchString + "," + startRow + "," + endRow + "\n");
            double rootNumber = root(startRow, endRow, data2);
            String rootValue = (rootNumber == (int) rootNumber) ? String.valueOf((int) rootNumber) : String.valueOf(rootNumber);
            writer.append(rootValue + "\n");
            writer.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void mode1task3(int start , int end) 
    {
        copyData2();
        String csvFile = "data2.csv";
        String line;
        int lineCount = 0;
        int[][] a = new int[1][1];
        double max1 = 0.00;
        double max2 = 0.00;
        double max3 = 0.00;
        String[][] output = new String[2][3];
        String[][] data = new String[31][10000];
        double[][] sum = new double[1][10000];
        double[][] ave = new double[1][10000];
        double[][] square = new double[1][10000];
        double[][] squareAve = new double[1][10000];
        double[][] finalRoot = new double[1][10000];
        for (int i = 0; i < data.length; i++) 
        {
            for (int j = 0; j < data[i].length; j++) 
            {
                data[i][j] = "";
            }
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) 
        {
            while ((line = br.readLine()) != null) 
            {
                String[] substrings = line.split(",");
                int count = substrings.length;
                a[0][0]= count ;
                for(int i = 0 ; i < count ; i++) 
                {
                    data[lineCount][i] = substrings[i];
                }
                lineCount++;
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        try
        {
            File file = new File("output.csv");
            boolean fileExists = file.exists();
            FileWriter writer = new FileWriter(fileExists ? "output.csv" : "output.csv", true);
            for(int i = 0 ; i < a[0][0] ; i++)
            {
                for(int k = start ; k < end+1 ;k++)
                {
                    sum[0][i] = sum[0][i]+Double.parseDouble(data[k][i]);
                }
            }
            for(int i = 0 ; i < a[0][0] ; i++)
            {
                ave[0][i] = sum[0][i]/(end-start+1);
            }
            for(int i = 0 ; i < a[0][0] ; i++)
            {
                for(int k = start ; k < end+1 ;k++)
                {
                    square[0][i] = square[0][i]+ (Double.parseDouble(data[k][i])-ave[0][i])*(Double.parseDouble(data[k][i])-ave[0][i]);
                }
            }
            for(int i = 0 ; i < a[0][0] ; i++)
            {
                squareAve[0][i] = square[0][i]/(end-start);
            }
            for(int i = 0 ; i < a[0][0] ; i++)
            {
                double epsilon = 0.00001;
                double guess = squareAve[0][i] / 2.0;
                while (doubleabsnumber(guess * guess - squareAve[0][i]) >= epsilon)
                {
                    guess = (guess + squareAve[0][i] / guess) / 2.0;
                }
                double rootNumber = roundToTwoDecimalPlaces(guess);
                finalRoot[0][i] = rootNumber;
                if(finalRoot[0][i]>max1)
                {
                    max3 = max2 ;
                    output[0][2] = output[0][1];
                    output[1][2] = output[1][1];
                    max2 = max1 ;
                    output[0][1] = output[0][0];
                    output[1][1] = output[1][0];
                    max1 = finalRoot[0][i];
                    output[1][0] = (rootNumber % 1 == 0) ? String.valueOf((int) rootNumber) : String.valueOf(rootNumber);
                    output[0][0] = data[0][i];
                }
                else if(finalRoot[0][i] < max1 && finalRoot[0][i] > max2)
                {
                    max3 = max2 ;
                    output[0][2] = output[0][1];
                    output[1][2] = output[1][1];
                    max2 =finalRoot[0][i] ;
                    output[0][1] = data[0][i];
                    output[1][1] = (rootNumber % 1 == 0) ? String.valueOf((int) rootNumber) : String.valueOf(rootNumber);
                }
                else if(finalRoot[0][i] < max1 && finalRoot[0][i] < max2 && finalRoot[0][i] > max3)
                {
                    max3 =finalRoot[0][i] ;
                    output[0][2] = data[0][i];
                    output[1][2] = (rootNumber % 1 == 0) ? String.valueOf((int) rootNumber) : String.valueOf(rootNumber);
                }
            }
            writer.append(output[0][0]+","+output[0][1]+","+output[0][2]+","+ start+","+end+"\n"+output[1][0]+","+output[1][1]+","+output[1][2]+"\n");
            writer.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public static void mode1task4(String searchString ,int start , int end) 
    {
        copyData2();
        String csvFile = "data2.csv";
        String line;
        double[][] databb = new double[1][2];
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile)))
        {
            int lineCount = 1;
            String[] headers = null;
            int[][] place = new int[1][1];
            double sumX = 0.00 ;
            double sumY = 0.00 ; 
            double sumXY = 0;
            double sumXSquare = 0;
            int n = end - start + 1;
            while ((line = br.readLine()) != null && lineCount < end+2)
            {
                if(lineCount == 1)
                {
                    headers = line.split(",");
                    for (int i = 0; i < headers.length; i++)
                    {
                        if (searchString.equals(headers[i]))
                        {
                            place[0][0] = i ;
                            break;
                        }
                    }
                }
                else if(lineCount>start && lineCount < end+1)
                {
                    String[] values = line.split(",");
                    sumX += (lineCount-1);
                    sumY += Double.parseDouble(values[place[0][0]]);
                    sumXY += (lineCount-1) * Double.parseDouble(values[place[0][0]]);
                    sumXSquare += (lineCount-1) * (lineCount-1);
                }
                else if (lineCount == end+1)
                {
                    String[] values = line.split(",");
                    sumX += (lineCount-1);
                    sumY += Double.parseDouble(values[place[0][0]]);
                    sumXY += (lineCount-1) * Double.parseDouble(values[place[0][0]]);
                    sumXSquare += (lineCount-1) * (lineCount-1);
                    double meanX = sumX / n;
                    double meanY = sumY / n;
                    double b1 = (sumXY - n * meanX * meanY) / (sumXSquare - n * meanX * meanX);
                    double b0 = meanY - b1 * meanX;
                    databb[0][0] = b0 ;
                    databb[0][1] = b1 ;
                }
                lineCount++ ;
                
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            File file = new File("output.csv");
            boolean fileExists = file.exists();
            FileWriter writer = new FileWriter(fileExists ? "output.csv" : "output.csv", true);
            writer.append(searchString+","+start+","+end +"\n"+ roundToTwoDecimalPlaces(databb[0][1])+","+ roundToTwoDecimalPlaces(databb[0][0])+"\n");
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static double roundToTwoDecimalPlaces(double number)
    {
        DecimalFormat df = new DecimalFormat("0.00");
        String formatted = df.format(number);
        
        if (formatted.endsWith(".00")) {
            return Double.parseDouble(formatted.replaceAll("\\..*$", ""));
        }
        
        return Double.parseDouble(formatted);
    }

    public static double root(int start , int end , double[][] data2 )
    {
        double sum = 0.00;
        double count = 0.00 ;
        for(int i = 0 ; i < end - start + 1 ; i++)
        {
            sum += data2[0][i];
            count++ ;
        }
        double average = sum / count;
        double aveSum = 0.00 ;
        for( int k = 0 ; k < count ; k++)
        {
            aveSum = aveSum + (data2[0][k]-average)*(data2[0][k]-average);
        }
        double number = aveSum/(count-1);
        double epsilon = 0.00001;
        double guess = number / 2.0;

        double absNumber = guess * guess - number ;
        double trueabsNumber = absNumber < 0 ? -absNumber : absNumber ;
        while ( doubleabsnumber(guess * guess - number) >= epsilon)
        {
            guess = (guess + number / guess) / 2.0;
        }
        double rootNumber = roundToTwoDecimalPlaces(guess);
        return rootNumber ;
    }

    public static double doubleabsnumber(double num)
    {
        if(num < 0 )
        {
            return -num ;
        }
        else
        {
            return num ;
        }
    }
    
    private static void copyData2()
    {
        try (BufferedReader reader = new BufferedReader(new FileReader("data.csv"));
        FileWriter writer = new FileWriter("data2.csv"))
        {
            String line;
            int count = 0; 
    
            while ((line = reader.readLine()) != null && count < 31) 
            {
                if(count < 30)
                {
                    writer.write(line);
                    writer.write("\n");
                }
                else
                {
                    writer.write(line);
                }
                count++; 
            }
            writer.close();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


}
