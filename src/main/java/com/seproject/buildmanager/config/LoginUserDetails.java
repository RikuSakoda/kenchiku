package com.seproject.buildmanager.config;

import java.util.Arrays;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.seproject.buildmanager.entity.MstUser;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class LoginUserDetails implements UserDetails {

  private final Integer userId;
  private final String email;
  private final String password;
  private final String name;
  private final Collection<? extends GrantedAuthority> authorities;

  public LoginUserDetails(MstUser user) {
    this.userId = user.getId();
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.name = user.getLName() + user.getFName();
    String[] auth = user.getRole().split(",");
    for (String a : auth) {
      a = "ROLE_" + a;
    }
    this.authorities = Arrays.stream(user.getRole().split(","))
        .map(role -> new SimpleGrantedAuthority(role)).toList();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // ロールのコレクションを返す
    return this.authorities;
  }

  public Integer getUserId() {
    // Idを返す
    return this.userId;
  }

  @Override
  public String getPassword() {
    // パスワードを返す
    return this.password;
  }

  @Override
  public String getUsername() {
    // ログイン名を返す
    return this.name;
  }

  public String getName() {
    // ユーザー名を返す
    return this.name;
  }

  @Override
  public boolean isAccountNonExpired() {
    // ユーザーが期限切れでなければtrueを返す
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // ユーザーがロックされていなければtrueを返す
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // パスワードが期限切れでなければtrueを返す
    return true;
  }

  @Override
  public boolean isEnabled() {
    // ユーザーが有効ならtrueを返す
    return true;
  }
}
