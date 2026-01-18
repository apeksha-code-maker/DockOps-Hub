import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class MetricsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String cpu = exec("top -bn1 | grep 'Cpu(s)' | awk '{print $2+$4}'");
        String memory = exec("free | grep Mem | awk '{print $3/$2 * 100.0}'");
        String disk = exec("df / | tail -1 | awk '{print $5}'");

        out.println("{");
        out.println("  \"cpu\": \"" + cpu.trim() + "\",");
        out.println("  \"memory\": \"" + memory.trim() + "\",");
        out.println("  \"disk\": \"" + disk.trim() + "\"");
        out.println("}");
    }

    private String exec(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Process p = Runtime.getRuntime().exec(
                new String[]{"bash", "-c", command}
            );
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(p.getInputStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        } catch (Exception e) {
            output.append("error");
        }
        return output.toString();
    }
}

