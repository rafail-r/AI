package robots;
import java.util.*;


public class astar{
	static ArrayList<node> compute(node start, node goal, node [][] maptemp, int mode){
		int i, j;
		int N = maptemp.length-1;
		int M = maptemp[0].length-1;
		node neighbor = null;
		node[][] map = new node[N+1][M+1];
		for (i=1; i<=N; i++){
        	for (j=1; j<=M; j++){
        		map[i][j] = new node();
        		map[i][j].x = maptemp[i][j].x; 
        		map[i][j].y = maptemp[i][j].y;
        		map[i][j].g = 0;
        		map[i][j].h = 0;
        		map[i][j].obstacle = maptemp[i][j].obstacle; 
        		map[i][j].closed = false;
        		map[i][j].open = false;
        	}
        }
		Comparator<node> comparator = new nodeComparator();
		PriorityQueue<node> openList = new PriorityQueue<node>(10, comparator);
		ArrayList<node> closedList = new ArrayList<node>();
		ArrayList<node> path = new ArrayList<node>();
		openList.add(map[start.x][start.y]);
		map[start.x][start.y].open = true;
		node current;
		while(!openList.isEmpty()) {
			current = openList.peek();
			if (current.x == goal.x && current.y == goal.y){
				path.add(current);
				while (current.parent != null){
					current = current.parent;
					path.add(current);
				}
				path = reverse(path);
				return path;
			}
			openList.remove();
			current.open = false;
			closedList.add(current);
			current.closed = true;
			loop:
			for (i=0; i<4; i++){
				switch (i){
					case 0: if (current.x+1 <= N) {
								neighbor = map[current.x+1][current.y]; break;
							} else continue loop;
					case 1: if (current.y+1 <= M) {
								neighbor = map[current.x][current.y+1]; break;
							} else continue loop;
					case 2: if (current.x-1 > 0) {
								neighbor = map[current.x-1][current.y]; break;
							} else continue loop;
					case 3: if (current.y-1 > 0) {
								neighbor = map[current.x][current.y-1]; break;
							} else continue loop;
				}
				if (neighbor.closed){
					continue loop;
				}
				if (!neighbor.obstacle){
					int newg = current.g + 1;
					int newh;
					if (mode == 1)
						newh = Math.abs(goal.x - neighbor.x) + Math.abs(goal.y - neighbor.y);
					else 
						newh = (int) (Math.pow((goal.x - neighbor.x), 2) + Math.pow((goal.y - neighbor.y), 2));						
					if (!neighbor.open){
						neighbor.open = true;
						neighbor.g = newg;
						neighbor.h = newh;
						neighbor.parent = current;
						openList.add(neighbor);
					}
					else if ((neighbor.g + neighbor.h) > (newg + newh)){
						neighbor.g = newg;
						neighbor.h = newh;
						neighbor.parent = current;
					}
				}
			}
		}
		return null;
	}
	
	public static class nodeComparator implements Comparator<node>
	{
			@Override
		    public int compare(node node1, node node2)
		    {
		        return (node1.g + node1.h) - (node2.g + node2.h);
		    }
	}
	public static ArrayList<node> reverse(ArrayList<node> list) {
		ArrayList<node> temp = new ArrayList<node>();
		for(int i = list.size()-1; i >= 0; i--) {
	        temp.add(list.get(i));
	        list.remove(i);
	    }
	    return temp;
	}
}