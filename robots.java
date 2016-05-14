package robots;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class robots {
	public static class inputs{
		int N;
		int M;
		int meets = 0;
		node start1 = null;
		node start2 = null;
		node goal = null;
		node[][] map = null;
		ArrayList<node> meetings = new ArrayList<node>();
		File file;
	}
	
	public static class Paths{
		ArrayList<node> path1 = new ArrayList<node>();
		ArrayList<node> path2 = new ArrayList<node>();
	}
	
	
	public static void main(String[] args) throws IOException{
		inputs input = new inputs();
        input.file = new File(args[0]);
		input = readinput(input);
		int mode = Integer.parseInt(args[1]);
		Paths paths = new Paths();
		Paths temppaths = new Paths();
		
		paths.path1 = astar.compute(input.start1, input.meetings.get(input.meetings.size()-1), input.map, mode);
		paths.path2 = astar.compute(input.start2, input.meetings.get(input.meetings.size()-1), input.map, mode);
		paths = resolvecollisions(paths);
		paths = resolvegoals(paths);
		for (int i=0; i<input.meets; i++){
			temppaths.path1 = astar.compute(paths.path1.get(paths.path1.size()-1), input.meetings.get(i), input.map, mode);
			
			temppaths.path2 = astar.compute(paths.path2.get(paths.path2.size()-1), input.meetings.get(i), input.map, mode);
			temppaths = resolvecollisions(temppaths);
			temppaths = resolvegoals(temppaths);
			
			temppaths.path1.remove(0);
			paths.path1.addAll(temppaths.path1);
			temppaths.path2.remove(0);
			paths.path2.addAll(temppaths.path2);
		}
		temppaths.path1 = astar.compute(paths.path1.get(paths.path1.size()-1), input.start1, input.map, mode);
		temppaths.path2 = astar.compute(paths.path2.get(paths.path2.size()-1), input.start2, input.map, mode);
		temppaths = resolvecollisions(temppaths);

		temppaths.path1.remove(0);
		paths.path1.addAll(temppaths.path1);
		temppaths.path2.remove(0);
		paths.path2.addAll(temppaths.path2);
		
		printpath(paths.path1);
		printpath(paths.path2);
		
		
		
	}

	private static Paths resolvecollisions(Paths paths) {
		int i;
		if (paths.path1.size() == paths.path2.size()){
			for (i=1; i<paths.path1.size(); i++){
				if ((paths.path1.get(i).x == paths.path2.get(i).x) && (paths.path1.get(i).y == paths.path2.get(i).y)){
					paths.path1.add(0, paths.path1.get(0));
					break;
				}
			}
		}
		return paths;
	}
	private static Paths resolvegoals(Paths paths) {
		if (paths.path1.size() == paths.path2.size()){
			paths.path1.remove(paths.path1.size()-1);
		}
		else if (paths.path1.size() > paths.path2.size()){
			paths.path1.remove(paths.path1.size()-1);
		}
		else paths.path2.remove(paths.path2.size()-1);
		return paths;
	}

	private static void printpath(ArrayList<node> path) {
		int i;
		System.out.println("Path =");
		for (i=0; i<path.size(); i++){
			System.out.println(path.get(i).x + " " + path.get(i).y);
		}
	}

	private static inputs readinput(inputs input) throws FileNotFoundException {
		Scanner xscan = new Scanner(input.file);
		node meeting;
		int i, j;
        input.N = xscan.nextInt();
        input.M = xscan.nextInt();
        xscan.nextLine();
        input.start1 = new node();
        input.start1.x = xscan.nextInt();
        input.start1.y = xscan.nextInt();
        xscan.nextLine();
        input.start2 = new node();
        input.start2.x = xscan.nextInt();
        input.start2.y = xscan.nextInt();
        xscan.nextLine();
        input.goal = new node();
        input.goal.x = xscan.nextInt();
        input.goal.y = xscan.nextInt();
        
        xscan.nextLine();
        input.meets = xscan.nextInt();
        for (i=0; i<input.meets; i++){
        	xscan.nextLine();
            meeting = new node();
            meeting.x = xscan.nextInt();
            meeting.y = xscan.nextInt();
            input.meetings.add(meeting);
        }
        input.meetings.add(input.goal);
        
        String line;
        xscan.nextLine();
        char[][] obstacles = new char[input.N+1][input.M+1];
        input.map = new node[input.N+1][input.M+1];
        for (i=1; i<=input.N; i++){
        	line = xscan.next();
        	obstacles[i] = line.toCharArray();
        }
        for (i=1; i<=input.N; i++){
        	for (j=1; j<=input.M; j++){
        		input.map[i][j] = new node();
        		input.map[i][j].x = i; 
        		input.map[i][j].y = j;
        		if (obstacles[i][j-1] == 'X'){
        			input.map[i][j].obstacle = true;
        		}
        		else {
        			input.map[i][j].obstacle = false;
        		}
        	}
        }
        xscan.close();
        return input;
	}

	@SuppressWarnings("unused")
	private static void test_print_input(int N, int M, int meets, List<node> meetings, node[][] map) {
		int i, j;
		System.out.println("Meetings:");
        for(i=0; i<meets; i++) {
            System.out.println(meetings.get(i).x + " " + meetings.get(i).y);
        }
        System.out.println("Map:");
        for (i=1; i<=N; i++){
        	for (j=1; j<=M; j++){
        		if (map[i][j].obstacle) System.out.print("1 ");
        		else System.out.print("0 ");
        	}
        	System.out.println();
        }	
	}
}