package LTChess.UCI;

public class TestString
{
	public static void main(String[] args) 
	{
		String str = "bestmove h7h8g";
		String result = "";
		try
		{
			result = str.substring(str.indexOf(" ")+1, str.indexOf(" ", str.indexOf(" ")+1));
		}
		catch (StringIndexOutOfBoundsException e)
		{
			result = str.substring(str.indexOf(" ")+1);
		}
		System.out.println(result);
		System.out.println(result.substring(4));
	}
}
