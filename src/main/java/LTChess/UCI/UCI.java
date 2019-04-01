package LTChess.UCI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * BIG NOTE: I DON'T WRITE THIS PIECE OF CODE. SOURCE: https://github.com/rahular/chess-misc/blob/master/JavaStockfish/src/com/rahul/stockfish/StockfishTest.java
 * A simple and efficient client to run Chess Engine from Java
 *   
 * @author Rahul A R
 * 
 */
public class UCI {

	public static Process engineProcess;
	private BufferedReader processReader;
	private OutputStreamWriter processWriter;
	private String PATH = "";
	private UCILevel level;

	/**
	 * Starts Stockfish engine as a process and initializes it
	 * 
	 * @param None
	 * @return True on success. False otherwise
	 */
	public UCI(String path, UCILevel level)
	{
		this.PATH = path;
		this.level = level;
	}
	
	public boolean startEngine() {
		try {
			engineProcess = Runtime.getRuntime().exec(PATH);
			processReader = new BufferedReader(new InputStreamReader(
					engineProcess.getInputStream()));
			processWriter = new OutputStreamWriter(
					engineProcess.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Takes in any valid UCI command and executes it
	 * 
	 * @param command
	 */
	public void sendCommand(String command) {
		try {
			processWriter.write(command + "\n");
			processWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is generally called right after 'sendCommand' for getting the raw
	 * output from Stockfish
	 * 
	 * @param waitTime
	 *            Time in milliseconds for which the function waits before
	 *            reading the output. Useful when a long running command is
	 *            executed
	 * @return Raw output from Stockfish
	 */
	public String getOutput(int waitTime) {
		StringBuffer buffer = new StringBuffer();
		try {
			Thread.sleep(waitTime);
			sendCommand("isready");
			while (true) {
				String text = processReader.readLine();
				if (text.equals("readyok"))
					break;
				else
					buffer.append(text + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
	
	public String readIt() throws IOException
	{
		StringBuffer buffer = new StringBuffer();
		try {
			Thread.sleep(1);
			while (true) {
				String text = processReader.readLine();
				if (text.contains("bestmove"))
				{
					buffer.append(text + "\n");
					break;
				}
			}
		} catch (Exception e) {
			
		}
		return buffer.toString();
	}
	

	/**
	 * This function returns the best move for a given position after
	 * calculating for 'waitTime' ms
	 * 
	 * @param fen
	 *            Position string
	 * @param waitTime
	 *            in milliseconds
	 * @return Best Move in PGN format
	 */
	public String getBestMove(String fen, int waitTime) {
		sendCommand("position fen " + fen);
		sendCommand("go movetime " + waitTime);
		return getOutput(waitTime + 20);
	}

	/**
	 * Stops Stockfish and cleans up before closing it
	 */
	public void stopEngine() {
		try {
			sendCommand("quit");
			processReader.close();
			processWriter.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Get a list of all legal moves from the given position
	 * 
	 * @param fen
	 *            Position string
	 * @return String of moves
	 */
	public String getLegalMoves(String fen) {
		sendCommand("position fen " + fen);
		sendCommand("d");
		return getOutput(0);
	}

	/**
	 * Draws the current state of the chess board
	 * 
	 * @param fen
	 *            Position string
	 */


	
	public String getUCIPath()
	{
		return this.PATH;
	}
	
	public void setUCILevel(UCILevel level)
	{
		this.level = level;
	}
	
	public UCILevel getUCILevel()
	{
		return level;
	}

	
}
