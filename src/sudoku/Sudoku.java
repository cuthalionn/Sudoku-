/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package sudoku;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;

/**
 *
 * @author taha
 */
public class Sudoku {
    
    /**
     * @param args the command line arguments
     * args[0]-> Name of the text file containing the puzzles
     */
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(args[0]);
        Scanner scan = new Scanner(file);
        int sum=0;
         long millis1 = System.nanoTime();
        while(scan.hasNext())
        {
           
            scan.nextLine();
            int[][] matrix = new int[9][9];
            for(int i=0;i<9;i++){
                String row = scan.nextLine();
                for(int j=0 ;j<9;j++){
                    matrix[i][j]= Integer.parseInt(""+row.charAt(j));
                }
            }
            //Markup
            ArrayList<Markup> markups = new ArrayList<>();
            fillMarkup(matrix,markups);
            
            boolean candidateChange=true;
            boolean placeFindingChange =true;
            //Candidate Check& Place Finding
            while(candidateChange || placeFindingChange){
                candidateChange = CandidateCheck(matrix,markups);
                placeFindingChange= placeFinding(matrix,markups);
            }
            //We keep processing this loop unttil both of the candidateCheck and Place Finding results in no change in the structure of the matrix.
            //After that we fill the rest of the matrix using brute-force
            solveLast(matrix);
            for(int i=0;i<9;i++){
                System.out.println();
                for(int j=0 ;j<9;j++){
                    System.out.print(matrix [i][j]+" ");
                }
            }
            String sum1 =""+ matrix[0][0]+matrix[0][1]+matrix[0][2];
            sum +=Integer.parseInt(sum1);
            System.out.println();
            System.out.println();
        }
        long millis2 = System.nanoTime();
        long dif =millis2-millis1;
        dif = dif %1000;
        System.out.println("Puzzles solved in "+dif+" milisecond/s");
    }
    private static boolean solveLast(int[][] matrix )
    {
        ArrayList<Integer> estimations = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        int row=0,column=0;
        boolean found =false;
        for(int i=0;i<9;i++)
        {
            for(int j=0 ;j<9;j++){
                if(matrix [i][j] == 0){
                    found = true;
                    row = i;
                    column = j;
                    break;
                }
            }
            if(found) break;
        }
        
        //fill estimations here
        //Check the row first
        for(int i=0;i<9;i++){
            if(estimations.contains(matrix[row][i]))
                estimations.remove(estimations.indexOf(matrix[row][i]));
        }
        
        //Check Column
        for(int i=0;i<9;i++)
            if(estimations.contains(matrix[i][column]))
                estimations.remove(estimations.indexOf(matrix[i][column]));
        
        //Check square
        int rowCheck,columnCheck;
        if(row<3)rowCheck=0;
        else if(row<6)rowCheck=3;
        else rowCheck=6;
        
        if(column<3)columnCheck=0;
        else if(column<6)columnCheck=3;
        else columnCheck=6;
        
        //rowCheck and columnCheck are the limits that our 0 is in.
        for(int i=rowCheck ;i<rowCheck+3;i++)
            for(int j=columnCheck;j<columnCheck+3;j++)
            {
                if(estimations.contains(matrix[i][j]))
                    estimations.remove(estimations.indexOf(matrix[i][j]));
            }
        //call recursively here
        
        while(found)
        {
            //We are in so we have found a 0 however we do not have any estimations
            if (estimations.isEmpty())
            {
                matrix[row][column]= 0;
                return false;
            }
            
            
            matrix[row][column]= estimations.get(0);
            estimations.remove(0);
            boolean solved = solveLast(matrix);
            if(solved) found =false;
        }
        
        return true;
    }
    
    private static boolean CandidateCheck(int[][] matrix,ArrayList<Markup> markups) 
    {
        boolean change = false;
        for(int i=0;i<markups.size();i++)
        {
            if(markups.get(i).marks.size()==1)
            {
                matrix[markups.get(i).cell.i][markups.get(i).cell.j] = markups.get(i).marks.get(0);
                markups.remove(i);
                change = true;
            }
        }
        return change;
    }
    
    private static void fillMarkup(int[][] matrix, ArrayList<Markup> markups) 
    {
        int row=0,column=-1;
        int markupCount=-1;
        while(row <=8 && column<=8){
            boolean found =false;
            for(int i=row;i<9;i++)
            {
                
                for(int j=column+1;j<9;j++){
                    if(matrix[i][j] == 0){
                        row = i;
                        column=j;
                        found=true;
                        markupCount++;
                        break;
                    }
                }
                if(found) break;
                else column=-1;
            }
            if(!found) break;
            //At this point we have a 0 in  row-column pair and will mark it up
            Markup markup = new Markup(row,column);
            markups.add(markup);
            ArrayList<Integer> estimations = markup.marks;
            
            //fill estimations here
            //Check the row first
            for(int i=0;i<9;i++){
                if(estimations.contains(matrix[row][i]))
                    estimations.remove(estimations.indexOf(matrix[row][i]));
            }
            
            //Check Column
            for(int i=0;i<9;i++)
                if(estimations.contains(matrix[i][column]))
                    estimations.remove(estimations.indexOf(matrix[i][column]));
            
            //Check square
            int rowCheck,columnCheck;
            if(row<3)rowCheck=0;
            else if(row<6)rowCheck=3;
            else rowCheck=6;
            
            if(column<3)columnCheck=0;
            else if(column<6)columnCheck=3;
            else columnCheck=6;
            
            //rowCheck and columnCheck are the limits that our 0 is in.
            for(int i=rowCheck ;i<rowCheck+3;i++)
                for(int j=columnCheck;j<columnCheck+3;j++)
                {
                    if(estimations.contains(matrix[i][j]))
                        estimations.remove(estimations.indexOf(matrix[i][j]));
                }
        }
    }
    
    private static boolean placeFinding(int[][] matrix, ArrayList<Markup> markups) 
    {
        boolean placeFindingChange = false;
        
        //Check all rows
        for(int i=0;i<9;i++)
        {
            int row = i;
            ArrayList<Integer> marks = new ArrayList<>();//marks all the cells that are located in current row and markuped
            for(int j=0;j<markups.size();j++)
            {
                if(markups.get(j).cell.i==row)
                    marks.add(j);
                else if (markups.get(j).cell.i>row)
                    break;
            }
            
            ArrayList<Integer> nonExistings = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
            for(int c=0;c<9;c++)
                if(nonExistings.contains(new Integer(matrix[i][c])))
                    nonExistings.remove(new Integer(matrix[i][c]));
            //Check only non existing numbers
            for(int ind=0;ind<nonExistings.size();ind++)
            {
                int j=nonExistings.get(ind);
                //for all columns of a row
                int rowChange=-1;
                int columnChange=-1;
                boolean foundOnce = false;
                int marksSign=-1;
                for(int k=0;k<marks.size();k++)
                {
                    if(markups.get(marks.get(k)).marks.contains(j) )
                    {
                        if(foundOnce){
                            foundOnce=false;
                            break;
                        }
                        //If it is the only place it could be found we will put it there
                        //If it is found more than once no changes will be made
                        rowChange=markups.get(marks.get(k)).cell.i;
                        columnChange=markups.get(marks.get(k)).cell.j;
                        foundOnce=true;
                        marksSign=k;
                    }
                }
                if(foundOnce)
                {
                    //It means there is only one possible place for j in the puzzle
                    //So let us place it in is place
                    
                    matrix[rowChange][columnChange]=j;
                    int index = marks.get(marksSign);
                    markups.remove(index);//remove the mark up for current cell
                    
                    //We have to update the marks array since it might have shift after the delete operation
                    marks = new ArrayList<>();//marks all the cells that are located in current row and markuped
                    for(int t=0;t<markups.size();t++)
                    {
                        if(markups.get(t).cell.i==row)
                            marks.add(t);
                        else if (markups.get(t).cell.i>row)
                            break;
                    }
                    
                    placeFindingChange = true;
                }
            }
        }
        //Check all columns
        for(int i=0;i<9;i++)
        {
            int column = i;
            ArrayList<Integer> marks = new ArrayList<>();//marks all the cells that are located in current column and markuped
            for(int j=0;j<markups.size();j++)
            {
                if(markups.get(j).cell.j==column)
                    marks.add(j);
            }
            
            ArrayList<Integer> nonExistings = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
            for(int c=0;c<9;c++)
                if(nonExistings.contains(new Integer(matrix[c][i])))
                    nonExistings.remove(new Integer(matrix[c][i]));
            //Check only non existing numbers
            for(int ind=0;ind<nonExistings.size();ind++)
            {
                int j=nonExistings.get(ind);
                //for all columns of a row
                int rowChange=-1;
                int columnChange=-1;
                boolean foundOnce = false;
                int marksSign=-1;
                for(int k=0;k<marks.size();k++)
                {
                    if(markups.get(marks.get(k)).marks.contains(j) )
                    {
                        if(foundOnce){
                            foundOnce=false;
                            break;
                        }
                        //If it is the only place it could be found we will put it there
                        //If it is found more than once no changes will be made
                        rowChange=markups.get(marks.get(k)).cell.i;
                        columnChange=markups.get(marks.get(k)).cell.j;
                        foundOnce=true;
                        marksSign=k;
                    }
                }
                if(foundOnce)
                {
                    //It means there is only one possible place for j in the puzzle
                    //So let us place it in is place
                    
                    matrix[rowChange][columnChange]=j;
                    int index = marks.get(marksSign);
                    markups.remove(index);//remove the mark up for current cell
                    
                    //We have to update the marks array since it might have shift after the delete operation
                    marks = new ArrayList<>();//marks all the cells that are located in current row and markuped
                    for(int t=0;t<markups.size();t++)
                    {
                        if(markups.get(t).cell.j==column)
                            marks.add(t);
                    }
                    
                    placeFindingChange = true;
                }
            }
        }
        
        //Check all squares
        for(int i=0,j=3;i<7 && j<10;i+=3,j+=3)//row  limits
            for(int k=0,m=3;k<7 && m<10;k+=3,m+=3) // column limits
            {
                ArrayList<Integer> marks = new ArrayList<>();//marks all the cells that are located in current row and markuped
                for(int t=0;t<markups.size();t++)
                {
                    if(markups.get(t).cell.i>=i && markups.get(t).cell.i<j)
                        if(markups.get(t).cell.j>=k && markups.get(t).cell.j<m)
                            marks.add(t);
                }
                
                ArrayList<Integer> nonExistings = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
                for(int a=i;a<j;a++)
                    for(int b=k;b<m;b++)
                        if(nonExistings.contains(new Integer(matrix[a][b])))
                            nonExistings.remove(new Integer(matrix[a][b]));
                
                //Check only non existing numbers
                
                for(int ind=0;ind<nonExistings.size();ind++)
                {
                    int num=nonExistings.get(ind);
                    //for all squares
                    int rowChange=-1;
                    int columnChange=-1;
                    boolean foundOnce = false;
                    int marksSign=-1;
                    for(int marksInd=0;marksInd<marks.size();marksInd++)
                    {
                        if(markups.get(marks.get(marksInd)).marks.contains(num))
                        {
                            if(foundOnce){
                                foundOnce=false;
                                break;
                            }
                            //If it is the only place it could be found we will put it there
                            //If it is found more than once no changes will be made
                            rowChange=markups.get(marks.get(marksInd)).cell.i;
                            columnChange=markups.get(marks.get(marksInd)).cell.j;
                            foundOnce=true;
                            marksSign=marksInd;
                        }
                    }
                    if(foundOnce)
                    {
                        //It means there is only one possible place for j in the puzzle
                        //So let us place it in is place
                        
                        matrix[rowChange][columnChange]=num;
                        int index = marks.get(marksSign);
                        markups.remove(index);//remove the mark up for current cell
                        
                        //We have to update the marks array since it might have shift after the delete operation
                        marks = new ArrayList<>();//marks all the cells that are located in current row and markuped
                        for(int t=0;t<markups.size();t++)
                        {
                            if(markups.get(t).cell.i>=i && markups.get(t).cell.i<j)
                                if(markups.get(t).cell.j>=k && markups.get(t).cell.j<m)
                                    marks.add(t);
                        }
                        placeFindingChange = true;
                    }
                }
            }
        
        return placeFindingChange;
    }
}
class Cell
{
    int i;
    int j;
    
    public Cell(int i , int j)
    {
        this.i = i;
        this.j = j;
    }
    public boolean equals(Cell cell)
    {
        return this.i==cell.i && this.j==cell.j;
    }
}
class Markup
{
    Cell cell;
    ArrayList<Integer> marks = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
    
    public Markup(int i, int j)
    {
        cell=new Cell(i,j);
    }
    
}
