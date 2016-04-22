package s3.ai;


import java.util.List;
import s3.base.S3;
import s3.entities.S3PhysicalEntity;
import s3.util.Pair;


public class AStar {
	
	
	public static int pathDistance(double start_x, double start_y, double goal_x, double goal_y,
			S3PhysicalEntity i_entity, S3 the_game) {
		AStar a = new AStar(start_x,start_y,goal_x,goal_y,i_entity,the_game);
		List<Pair<Double, Double>> path = a.computePath();
		if (path!=null) return path.size();
		return -1;
	}

	public AStar(double start_x, double start_y, double goal_x, double goal_y,
			S3PhysicalEntity i_entity, S3 the_game) {
            // ...
	}

	public List<Pair<Double, Double>> computePath() {
            // ...
            return null;
	}

}
