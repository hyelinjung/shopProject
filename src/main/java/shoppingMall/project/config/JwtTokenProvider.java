package shoppingMall.project.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
//토큰을 생성하고 검증하는 클래스
//해당 컴포넌트는 필터클래스에서 사전검증을 거침
public class JwtTokenProvider {
    private String secretKey = "firstMyProjectSecretKeyabcdefghijklnmopqrstuvwxyzzzzzzzzzzzzz";
    //토큰 유효시간 30분
    private long access_tokenValidTime = 30 * 60 * 1000L;
    private long refresh_tokenValidTime = 60 * 60 * 24 * 12 * 1000L; //유효시간 2주

    private  final UserDetailsService userDetailsService;

    //객체 초기화, secretkey를 base64로 인코딩
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //jwt 생성
    public String createToken(String username,String role){
        Claims claims = Jwts.claims().setSubject(username); // JWT payload 에 저장되는 정보단위, 보통 여기서 user를 식별하는 값을 넣는다.
        claims.put("role",role); // 정보는 key / value 쌍으로 저장된다
        Date date = new Date();
        return  Jwts.builder()
                .setClaims(claims) //정보 저장
                .setIssuedAt(date) //토큰 발행 시간 정보
                .setExpiration(new Date(date.getTime() + access_tokenValidTime)) //유효 시간 저장
                .signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과 signature 에 들어갈 secret 겂 세팅
                .compact();
    }

    public String create_refresh_Token(String role){
        Claims claims = Jwts.claims();
        claims.put("role",role); // 정보는 key / value 쌍으로 저장된다
        Date date = new Date();
        return  Jwts.builder()
                .setClaims(claims) //정보 저장
                .setIssuedAt(date) //토큰 발행 시간 정보
                .setExpiration(new Date(date.getTime() + refresh_tokenValidTime)) //유효 시간 저장
                .signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과 signature 에 들어갈 secret 겂 세팅
                .compact();
    }

    //jwt 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }
    //토큰에서 회원 정보 추출
    public String getUserPk(String token){

        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }
    //request의 헤더에서 토큰 값을 가져옵니다.
    public String resolveToken( HttpServletRequest request){
        // System.out.println("가져온 헤더값:"+request.getHeader("Authorization") );
        Cookie[] cookies = request.getCookies();
        String value = "";
        if (cookies != null && cookies.length>0){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("accessToken")){
                    value = cookie.getValue();
                }
            }
        }else {
            return value;
        }
        return value;
    }
    //토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken){
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken);
            return !claimsJws.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            return false;
        }
    }
   /* public String validateRefreshToken(String token){
        String refreshToken = to
    }*/
}

