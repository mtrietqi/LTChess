package LTChess.Player;

/*
 * This class simply identifies if the player has made the move or not 
 */

public enum MoveResult implements java.io.Serializable
{ 
	DONE {
		@Override
		public boolean isDone() {
			return true;
		}

		@Override
		public boolean isInChecK() {
			return false;
		}
	},
	ILLEGAL{
		@Override
		public boolean isDone() {
			return false;
		}

		@Override
		public boolean isInChecK() {
			return false;
		}
	},IN_CHECK{

		@Override
		public boolean isDone() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isInChecK() {
			// TODO Auto-generated method stub
			return true;
		}
			
	};
	public abstract boolean isDone();
	public abstract boolean isInChecK();
}
