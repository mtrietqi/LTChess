package LTChess.UCI;

import java.io.IOException;

public class TestUCI 
{
	public static void main(String[] args) {
		UCI uci = new UCI("resource/engine/stockfish_8_x32.exe", UCILevel.NIGHTMARE);
		// initialize and connect to engine
		if (uci.startEngine()) {
			System.out.println("Engine has started..");
		} else {
			System.out.println("Oops! Something went wrong..");
		}

		// send commands manually
		uci.sendCommand("uci");
		// receive output dump
		System.out.println(uci.getOutput(0));
		uci.getOutput(0);
		uci.sendCommand("ucinewgame");
//		uci.sendCommand("position startpos moves g1f3 g8f6 d2d4 e7e6 d1d3 d7d5 g2g4 f6g4 d3a3 f8a3 b2a3 b8c6 f1h3 c6d4 h3g4 d4c2 e1g1");
//		uci.sendCommand("go wtime 251170 btime 265757 winc 0 binc 0");
		uci.sendCommand("position fen rnbqkbnr/pppppppp/8/8/6P1/8/PPPPPP1P/RNBQKBNR b KQkq P 0 1");
		uci.sendCommand("go wtime 298339 btime 299953 winc 0 binc 0");
//		System.out.println(uci.getBestMove("rnbqkbnr/pppp2pp/8/5p2/4p1P1/5N1B/PPPPPP1P/RNBQ1RK", 100));
		try {
			System.out.println(uci.readIt());
//			uci.drawBoard("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
