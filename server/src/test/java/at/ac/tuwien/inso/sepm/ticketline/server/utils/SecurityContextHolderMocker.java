package at.ac.tuwien.inso.sepm.ticketline.server.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Principal.Role;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PrincipalRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PrincipalService;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.security.core.context.SecurityContextHolder;
/**
 * Created by Calvin on 11.06.17.
 */
public class SecurityContextHolderMocker {

    public static void setSecurityContextToUsername(String username) {
        SecurityContextHolder.setContext(new SecurityContext() {
            @Override
            public Authentication getAuthentication() {
                return new Authentication() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return null;
                    }

                    @Override
                    public Object getCredentials() {
                        return null;
                    }

                    @Override
                    public Object getDetails() {
                        return null;
                    }

                    @Override
                    public Object getPrincipal() {
                        return new User(username, "password",
                            new Collection<GrantedAuthority>() {
                                @Override
                                public int size() {
                                    return 0;
                                }

                                @Override
                                public boolean isEmpty() {
                                    return false;
                                }

                                @Override
                                public boolean contains(Object o) {
                                    return false;
                                }

                                @Override
                                public Iterator<GrantedAuthority> iterator() {
                                    return new Iterator<GrantedAuthority>() {
                                        @Override
                                        public boolean hasNext() {
                                            return false;
                                        }

                                        @Override
                                        public GrantedAuthority next() {
                                            return null;
                                        }
                                    };
                                }

                                @Override
                                public Object[] toArray() {
                                    return new Object[0];
                                }

                                @Override
                                public <T> T[] toArray(T[] a) {
                                    return null;
                                }

                                @Override
                                public boolean add(GrantedAuthority grantedAuthority) {
                                    return false;
                                }

                                @Override
                                public boolean remove(Object o) {
                                    return false;
                                }

                                @Override
                                public boolean containsAll(Collection<?> c) {
                                    return false;
                                }

                                @Override
                                public boolean addAll(Collection<? extends GrantedAuthority> c) {
                                    return false;
                                }

                                @Override
                                public boolean removeAll(Collection<?> c) {
                                    return false;
                                }

                                @Override
                                public boolean retainAll(Collection<?> c) {
                                    return false;
                                }

                                @Override
                                public void clear() {

                                }
                            });
                    }

                    @Override
                    public boolean isAuthenticated() {
                        return false;
                    }

                    @Override
                    public void setAuthenticated(boolean b) throws IllegalArgumentException {

                    }

                    @Override
                    public String getName() {
                        return null;
                    }
                };
            }

            @Override
            public void setAuthentication(Authentication authentication) {

            }
        });
    }
}
