package com.pig4cloud.pig.common.security.service;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.entity.SysUser;
import com.pig4cloud.pig.admin.api.feign.RemoteUserService;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static cn.hutool.core.util.ArrayUtil.isNotEmpty;
import static com.pig4cloud.pig.common.core.constant.CommonConstants.STATUS_NORMAL;
import static com.pig4cloud.pig.common.core.constant.SecurityConstants.BCRYPT;
import static com.pig4cloud.pig.common.core.constant.SecurityConstants.ROLE;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

/**
 * 用户详细信息
 *
 * @author lengleng
 */
@Slf4j
@Component
@AllArgsConstructor
public class PigUserDetailsServiceImpl implements UserDetailsService {
	private final RemoteUserService remoteUserService;
	private final CacheManager cacheManager;

	/**
	 * 用户密码登录
	 *
	 * @param username 用户名
	 * @return
	 */
	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String username) {
		Cache cache = cacheManager.getCache("user_details");
		if (cache != null && cache.get(username) != null) {
			return (PigUser) cache.get(username).get();
		}

		R<UserInfo> result = remoteUserService.info(username, SecurityConstants.FROM_IN);
		UserDetails userDetails = getUserDetails(result);
		cache.put(username, userDetails);
		return userDetails;
	}

	/**
	 * 构建userdetails
	 *
	 * @param result 用户信息
	 * @return
	 */
	private UserDetails getUserDetails(R<UserInfo> result) {
		if (result == null || result.getData() == null) {
			throw new UsernameNotFoundException("用户不存在");
		}

		UserInfo info = result.getData();
		Set<String> dbAuthsSet = new HashSet<>();

		if (isNotEmpty(info.getRoles())) {
			// 获取角色
			 dbAuthsSet = asList(info.getRoles()).stream().map(it->ROLE + it).collect(toSet());
			// 获取资源
			dbAuthsSet.addAll(asList(info.getPermissions()));

		}

		Collection<? extends GrantedAuthority> authorities = createAuthorityList(dbAuthsSet.stream().toArray(String[]::new));
		SysUser user = info.getSysUser();

		// 构造security用户
		return new PigUser(user.getUserId(), user.getDeptId(), user.getUsername(), BCRYPT + user.getPassword(),
			StrUtil.equals(user.getLockFlag(), STATUS_NORMAL), true, true, true, authorities);
	}
}
