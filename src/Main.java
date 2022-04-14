import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	private static Maze maze;
	
	private static ArrayList<String[]> readInputFile(Scanner keyboard) {
		System.out.print(
		"Please enter the name (including extenstion and possibly path) of the txt file containing the map: ");
		Scanner file = null;
		ArrayList<String[]> map = new ArrayList<String[]>();
		do {
			try {
				file = new Scanner(new File(keyboard.nextLine()));
			} catch (Exception e) {
				System.out.print("Error: Something is wrong with the input file. Please try again: ");
				continue;
			}
			while (file.hasNextLine()) {
				map.add(file.nextLine().split("\t"));
				if (map.size() > 1 && map.get(map.size() - 2).length != map.get(map.size() - 1).length) {
					file = null;
					System.out.println("Error: map does not have a rectangular shape.");
					break;
				}
			}
			if (map.isEmpty() || map.get(0).length == 0) {
				System.out.println("Error: map is empty.");
				file = null;
			}
		} while (file == null);
		return map;
	}
	
	private static boolean interpretMap(ArrayList<String[]> input) {
		maze = new Maze(input.size(), input.get(0).length);
		for (int r = 0; r < maze.getRows(); r++)
		for (int c = 0; c < maze.getColumns(); c++) {
			String current = input.get(r)[c];
			if (current.equals("N"))
			continue;
			else if (current.equals("S"))
			maze.addStart(r, c);
			else if (current.equals("T"))
			maze.addTarget(r, c);
			else if (current.equals("DE"))
			maze.addDeadEnd(r, c);
			else {
				if (current.contains("XD"))
				maze.addNoDown(r, c);
				else if (current.contains("XU"))
				maze.addNoUp(r, c);
				else if (current.contains("D")) {
					Scanner cell = new Scanner(current.substring(current.indexOf("D") + 1));
					cell.useDelimiter("[^0-9]");
					try {
						maze.addVerticalJump(r, c, cell.nextInt());
					} catch (Exception exp) {
						cell.close();
						return false;// wrong format
					}
				} else if (current.contains("U")) {
					Scanner cell = new Scanner(current.substring(current.indexOf("U") + 1));
					cell.useDelimiter("[^0-9]");
					try {
						maze.addVerticalJump(r, c, -cell.nextInt());
					} catch (Exception exp) {
						cell.close();
						return false;// wrong format
					}
				}
				if (current.contains("XR"))
				maze.addNoRight(r, c);
				else if (current.contains("XL"))
				maze.addNoLeft(r, c);
				else if (current.contains("R")) {
					Scanner cell = new Scanner(current.substring(current.indexOf("R") + 1));
					cell.useDelimiter("[^0-9]");
					try {
						maze.addHorizontalJump(r, c, cell.nextInt());
					} catch (Exception exp) {
						cell.close();
						return false;// wrong format
					}
				} else if (current.contains("L")) {
					Scanner cell = new Scanner(current.substring(current.indexOf("L") + 1));
					cell.useDelimiter("[^0-9]");
					try {
						maze.addHorizontalJump(r, c, -cell.nextInt());
					} catch (Exception exp) {
						cell.close();
						return false;// wrong format
					}
				}
				if (current.contains("W")) {
					Scanner cell = new Scanner(current.substring(current.indexOf("W") + 1));
					cell.useDelimiter("[^0-9]");
					try {
						maze.addDelay(r, c, cell.nextInt());
					} catch (Exception exp) {
						cell.close();
						return false;// wrong format
					}
				}
			}
		}
		return true;// correct format
	}
	
	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		ArrayList<String[]> input = null;
		do {
			if (input != null)
			System.out.println("Error: Input file has wrong format!");
			input = readInputFile(keyboard);
			
		} while (!interpretMap(input));
		maze.solve();
		maze.printSolution();
	}
	
}
