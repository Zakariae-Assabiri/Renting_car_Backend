package Car.project.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class PermissionFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // Définir les endpoints publics (path, method)
    private final List<PublicEndpoint> publicEndpoints = List.of(
            new PublicEndpoint("/api/voitures/**", "GET"),
            new PublicEndpoint("/api/voitures/*/photo", "GET"),
            new PublicEndpoint("/api/auth/**", "ANY")
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        String requestMethod = request.getMethod(); // GET, POST, etc.

        boolean isPublic = publicEndpoints.stream()
                .anyMatch(endpoint ->
                        pathMatcher.match(endpoint.getPath(), requestPath) &&
                        (endpoint.getMethod().equalsIgnoreCase("ANY") || endpoint.getMethod().equalsIgnoreCase(requestMethod))
                );

        if (isPublic) {
            filterChain.doFilter(request, response);
            return;
        }

        //  Bypass permission check for contract generation
        if (pathMatcher.match("/api/contracts/**", requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (pathMatcher.match("/api/reservations/**", requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (pathMatcher.match("/api/clients/**", requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("Authorities: " + authentication.getAuthorities());

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                filterChain.doFilter(request, response);
                return;
            }

            boolean hasPermission = authentication.getAuthorities().stream()
                    .anyMatch(authority -> {
                        String[] parts = authority.getAuthority().split(":");
                        if (parts.length != 2) return false;
                        String pattern = parts[0];
                        String method = parts[1];
                        return pathMatcher.match(pattern, requestPath) && method.equalsIgnoreCase(requestMethod);
                    });

            if (!hasPermission) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // Petite classe interne pour représenter (path, method)
    static class PublicEndpoint {
        private final String path;
        private final String method;

        public PublicEndpoint(String path, String method) {
            this.path = path;
            this.method = method;
        }

        public String getPath() {
            return path;
        }

        public String getMethod() {
            return method;
        }
    }
}
