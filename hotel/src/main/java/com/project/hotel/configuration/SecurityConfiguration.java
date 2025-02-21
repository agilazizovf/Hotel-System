package com.project.hotel.configuration;

import com.project.hotel.filter.JwtAuthorizationFilter;
import com.project.hotel.service.impl.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {

    @Bean
    public static BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder passwordEncoder)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf->csrf.disable())
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers
                        .frameOptions().disable() // Disable frame options for development
                        .contentSecurityPolicy("frame-ancestors 'self'")
                )
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(permitAllUrls).permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/passwords/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/customers/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/hotels/create").hasAuthority("ADD_HOTEL")
                                .requestMatchers(HttpMethod.GET, "/api/hotels/get-all").hasAuthority("GET_ALL_HOTELS")
                                .requestMatchers(HttpMethod.GET, "/api/hotels/search-by-name").hasAuthority("SEARCH_HOTEL")
                                .requestMatchers(HttpMethod.GET, "/api/hotels/{hotelId}").hasAuthority("FIND_HOTEL_BY_ID")
                                .requestMatchers(HttpMethod.PUT, "/api/hotels/update/{hotelId}").hasAuthority("UPDATE_HOTEL")
                                .requestMatchers(HttpMethod.DELETE, "/api/hotels/delete/{hotelId}").hasAuthority("DELETE_HOTEL")

                                .requestMatchers(HttpMethod.POST, "/api/rooms/create").hasAuthority("ADD_ROOM")
                                .requestMatchers(HttpMethod.GET, "/api/rooms/get-all").hasAuthority("GET_ALL_ROOMS")
                                .requestMatchers(HttpMethod.GET, "/api/rooms/find-by-hotel/{hotelId}").hasAuthority("FIND_ROOMS_BY_HOTEL")
                                .requestMatchers(HttpMethod.GET, "/api/rooms/{roomId}").hasAuthority("FIND_ROOM_BY_ID")
                                .requestMatchers(HttpMethod.PUT, "/api/rooms/update/{roomId}").hasAuthority("UPDATE_ROOM")
                                .requestMatchers(HttpMethod.DELETE, "/api/rooms/delete/{roomId}").hasAuthority("DELETE_ROOM")

                                .requestMatchers(HttpMethod.POST, "/api/payments/process").hasAuthority("PROCESS_PAYMENT")
                                .requestMatchers(HttpMethod.GET, "/api/payments/get-all").hasAuthority("GET_ALL_PAYMENTS")
                                .requestMatchers(HttpMethod.GET, "/api/payments/by-payment/{paymentId}").hasAuthority("FIND_PAYMENT_BY_ID")
                                .requestMatchers(HttpMethod.GET, "/api/payments/by-hotel/{hotelId}").hasAuthority("FIND_PAYMENTS_BY_HOTEL")

                                .requestMatchers(HttpMethod.POST, "/api/reservations/add").hasAuthority("ADD_RESERVATION")
                                .requestMatchers(HttpMethod.GET, "/api/reservations/get-all").hasAuthority("GET_ALL_RESERVATIONS")
                                .requestMatchers(HttpMethod.GET, "/api/reservations/{reservationId}").hasAuthority("FIND_RESERVATION_BY_ID")
                                .requestMatchers(HttpMethod.GET, "/api/reservations/find-by-user").hasAuthority("FIND_RESERVATION_BY_USER")
                                .requestMatchers(HttpMethod.PUT, "/api/reservations/update/{reservationId}").hasAuthority("UPDATE_RESERVATION")
                                .requestMatchers(HttpMethod.PUT, "/api/reservations/confirm/{reservationId}").hasAuthority("CONFIRM_RESERVATION")
                                .requestMatchers(HttpMethod.PUT, "/api/reservations/complete/{reservationId}").hasAuthority("COMPLETE_RESERVATION")
                                .requestMatchers(HttpMethod.PUT, "/api/reservations/check-in/{reservationId}").hasAuthority("CHECK_IN_RESERVATION")
                                .requestMatchers(HttpMethod.DELETE, "/api/reservations/cancel/{reservationId}").hasAuthority("CANCEL_RESERVATION")

                                .requestMatchers(HttpMethod.POST, "/api/reviews/create").hasAuthority("ADD_REVIEW")
                                .requestMatchers(HttpMethod.GET, "/api/reviews/get-all").hasAuthority("GET_ALL_REVIEWS")
                                .requestMatchers(HttpMethod.GET, "/api/reviews/{reviewId}").hasAuthority("FIND_REVIEW_BY_ID")
                                .requestMatchers(HttpMethod.PUT, "/api/reviews/update/{reviewId}").hasAuthority("UPDATE_REVIEW")
                                .requestMatchers(HttpMethod.DELETE, "/api/reviews/delete/{reviewId}").hasAuthority("DELETE_REVIEW")

                                .requestMatchers(HttpMethod.POST, "/api/contents/save").hasAuthority("SAVE_FILE")
                                .requestMatchers(HttpMethod.POST, "/api/contents/savePicture/{hotelId}").hasAuthority("SAVE_PICTURE")
                                .requestMatchers(HttpMethod.PUT, "/api/contents/pictures/{id}/{hotelId}").hasAuthority("UPDATE_PICTURE")
                                //.requestMatchers(adminUrls).hasAnyAuthority("ROLE_ADMIN")
                                //.requestMatchers(clientUrls).hasAnyAuthority("ROLE_CUSTOMER")
                                .requestMatchers(anyAuthUrls).authenticated()
//                                .anyRequest().authenticated()
                ).exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) ->
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
                        )
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN)
                        )
                );
        return http.build();

    }

    // Url-leri qeyd ele
    static String[] permitAllUrls = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/h2-console/**"
//            "/api/auth/**",
//            "/api/admins/**",
//            "/api/hotels/**",
//            "/api/rooms/**",
//            "/api/customers/**",
//            "/api/reservations/**",
//            "/api/payments/**",
//            "/api/reviews/**"
    };

    static String[] adminUrls = {
            "/controller/admin"
    };

    static String[] clientUrls = {
            "/controller/client"
    };

    static String[] anyAuthUrls = {
            "/controller/any"
    };

}
