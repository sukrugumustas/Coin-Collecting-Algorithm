//Atakan ÜLGEN, 150115066
//Þükrü GÜMÜÞTAÞ, 150114032

//importing the necessary classes
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class hw3problem1 {
	public static void main (String [] args) {
		//if command line argument is empty the program will stop
		if (args.length == 0) {
			System.out.println("There is no file!");
			return;
		}
		
		//declaring variables
		String input;
		Scanner sc = null;
		//file location can be given as string and command line argument
		//is a string stored in args[0]
		File file = new File(args[0]);
		//this is our coin array
		int [][] coins;
		try {
			//if possible scan the file
			sc = new Scanner(file);
		} catch (Exception e) {
			//if not possible give the error message
			System.out.println("There is no such file!");
			return;
		}
		//row and column
		int row, column;
		//scanner can read the numbers or other thnings via its methods like nextInt, nextDouble
		//so if the right algorithm is created, the blank spaces will not be a problem
		row = sc.nextInt();
		column = sc.nextInt();
		//creating the coin array according to sizes
		coins = new int[row][column];
		//getting inputs one by one
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				//this string holds the inputs and if input is X since a string can not stored in
				//an int array, it has to be converted something else like -100, so the robot can
				//never visit that cell
				input = sc.next();
				if (input.charAt(0) == 'X') {
					coins[i][j] = -100;
				} else {
					coins[i][j] = Integer.parseInt(input);
				}
			}
		}
		//this is the real algorithm that does the magic
		collectCoins(coins, args[0]);
		sc.close();
	}
	
	private static void collectCoins(int [][] coinBoard, String filename) {
		//creating new array for calculations
		int [][] F = new int[coinBoard.length][coinBoard[0].length];
		F[0][0] = coinBoard[0][0];
		//making calculations according to algorithm
		for (int i = 1; i<coinBoard[0].length; i++) {
			F[0][i] = F[0][i-1] + coinBoard[0][i];
			//This code includes some comparisons in it, if a cell right or above
			//of the corresponding cell contains -100, program doesn’t have to
			//take maximum of that -100 cell and the other cell. Because -100 means
			//that robot can not visit that cell so it has to go to other cell.
			if (F[0][i]<0) {
				F[0][i] = -100;
			}
		}
		for (int i = 1; i<coinBoard.length; i++) {
			F[i][0] = F[i - 1][0] + coinBoard[i][0];
			if(F[i][0]<0) {
				F[i][0] = -100;
			}
			for (int j = 1; j<coinBoard[0].length; j++) {
				F[i][j] = max(F[i - 1][j], F[i][j-1]) + coinBoard[i][j];
				if(F[i][j]<0) {
					F[i][j] = -100;
				}
			}
		}
		//this code draws the path
		pathDriver(F, coinBoard, filename);
	}
	
	//this method returns max of two integers
	private static int max(int a, int b) {
		if (a>b) return a; else return b;
	}

	//this code converts integer array to string
	private static String [][] intToString (int[][] array) {
		int row = array.length;
		int column = array[0].length;
		String [][] returner = new String[row][column];
		for (int i = 0; i<row; i++) {
			for (int j = 0; j<column; j++) {
				if (array[i][j] == -100) {
					returner[i][j] = "X";
				} else {
					returner[i][j] = Integer.toString(array[i][j]);
				}
			}
		}
		return returner;
	}

	private static void pathDriver (int [][] array, int [][] coins, String filename) {
		//this string will be used to display output
		String [][] string = intToString(coins);
		int temp = array[0].length - 1, i, j, k = 0;
		//we find the input file name so we can add _output.txt to end of it and create output file
		while(filename.charAt(k)!='.') {
			k++;
		}
		//last element of array is path because it ends there
		string[array.length-1][array[0].length-1] = "P";
		for(i = array.length-1; i>=0; i--) {
			for (j = temp; j>=0; j--) {
				//compares up end left elements of that cell in second array and takes the greater
				//one and goes to it. Procedure is being processed until first element of array.
				string[i][j] = "P";
				if (j==0) {
					break;
				}
				if (i==0) {
					continue;
				}
				if (array[i][j-1]>=array[i-1][j]) {
					continue;
				} else {
					break;
				}
			}
			//this temp variable helps us finding last column iteration has benn done on. So it can move from
			//there is no need to start from last column again
			temp = j;
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename.substring(0, k) + "_output.txt")));
			writer.write(Integer.toString(array[array.length-1][array[0].length-1]));
			writer.newLine();
			for (i = 0; i<array.length; i++) {
				for (j = 0; j<array[0].length; j++) {
					writer.write(string[i][j] + "\t");
				}
				writer.newLine();
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
}