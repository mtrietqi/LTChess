package LTChess.UCI;

/*
 * This class represents the level of UCI 
 */

public enum UCILevel {
	AMATEUR
	{
		@Override
		public int getEngineLevel(){
			return 0;
		}
	},
	EASY
	{
		@Override
		public int getEngineLevel(){
			return 2;
		}
	},
	MEDIUM
	{
		@Override
		public int getEngineLevel(){
			return 6;
		}
	},
	HARD
	{
		@Override
		public int getEngineLevel(){
			return 10;
		}
	},
	VERYHARD
	{
		@Override
		public int getEngineLevel(){
			return 15;
		}
	},
	NIGHTMARE
	{
		@Override
		public int getEngineLevel(){
			return 20;
		}
	};
	
	
	public abstract int getEngineLevel();
}
