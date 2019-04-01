package LTChess.Group;

import LTChess.Player.BlackPlayer;
import LTChess.Player.Player;
import LTChess.Player.WhitePlayer;

/*
 * This class represents groups of pieces (black and white)
 */

public enum Group implements java.io.Serializable
{
	WHITE {
		@Override
		public int getDirection() {
			// TODO Auto-generated method stub
			return -1;
		}

		@Override
		public boolean isBlack() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isWhite() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public Player chooseaPlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
			// TODO Auto-generated method stub
			return whitePlayer;
		}

		@Override
		public int getOppositeMoving() {
			// TODO Auto-generated method stub
			return 1;
		}
	}
	,BLACK  {
		@Override
		public int getDirection() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public boolean isBlack() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isWhite() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Player chooseaPlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
			return blackPlayer;
		}

		@Override
		public int getOppositeMoving() {
			return -1;
		}

	};
	public abstract int getDirection();
	public abstract int getOppositeMoving();
	public abstract boolean isBlack();
	public abstract boolean isWhite();
	public abstract Player chooseaPlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
