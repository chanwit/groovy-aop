import org.codehaus.groovy.geelab.Console;

public class MatToolbox {

	static {
		try {
			Console.registerMethods();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
}
