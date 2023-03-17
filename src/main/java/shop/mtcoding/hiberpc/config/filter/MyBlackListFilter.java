package shop.mtcoding.hiberpc.config.filter;

import javax.servlet.*;
import java.io.IOException;

public class MyBlackListFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // ※주의 버퍼를 비우면 컨트롤러에서 버퍼를 읽지 못한다.
        // x-www-form-urlencoded
        String value = request.getParameter("value");
        if (value == null) {
            response.setContentType("text/plain; charset=utf-8");
            response.getWriter().println("value parameter를 전송해 주세요.");
            return;
        }

        if(value.equals("babo")) {
            response.setContentType("text/plain; charset=utf-8");
            response.getWriter().println("당신은 블랙리스트가 되었습니다.");
            return;
        }
        chain.doFilter(request ,response);
    }
}
