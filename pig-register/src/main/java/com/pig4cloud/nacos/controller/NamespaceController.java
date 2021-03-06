
package com.pig4cloud.nacos.controller;

import com.alibaba.nacos.config.server.model.RestResult;
import com.alibaba.nacos.config.server.model.TenantInfo;
import com.alibaba.nacos.config.server.service.PersistService;
import com.pig4cloud.nacos.model.Namespace;
import com.pig4cloud.nacos.model.NamespaceAllInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * namespace service
 *
 * @author Nacos
 */
@RestController
@RequestMapping("/v1/console/namespaces")
public class NamespaceController {

    @Autowired
    private PersistService persistService;

    /**
     * Get namespace list
     *
     * @param request  request
     * @param response response
     * @return namespace list
     */
    @GetMapping
    public RestResult<List<Namespace>> getNamespaces(HttpServletRequest request, HttpServletResponse response) {
        // TODO 获取用kp
        List<TenantInfo> tenantInfos = persistService.findTenantByKp("1");
        Namespace namespace0 = new Namespace("", "public", 200, persistService.configInfoCount(""), 0);

        List<Namespace> namespaces = tenantInfos.stream().map(tenantInfo->new Namespace(tenantInfo.getTenantId(), tenantInfo.getTenantName(), 200,
                persistService.configInfoCount(tenantInfo.getTenantId()), 2)).collect(toList());
        namespaces.add(namespace0);

        RestResult<List<Namespace>> rr = new RestResult<List<Namespace>>();
        rr.setCode(200);
        rr.setData(namespaces);
        return rr;
    }

    /**
     * get namespace all info by namespace id
     *
     * @param request     request
     * @param response    response
     * @param namespaceId namespaceId
     * @return namespace all info
     */
    @GetMapping(params = "show=all")
    public NamespaceAllInfo getNamespace(HttpServletRequest request, HttpServletResponse response,
                                         @RequestParam("namespaceId") String namespaceId) {
        // TODO 获取用kp
        if (StringUtils.isBlank(namespaceId)) {
            return new NamespaceAllInfo(namespaceId, "Public", 200,
                persistService.configInfoCount(""), 0, "Public Namespace");
        } else {
            TenantInfo tenantInfo = persistService.findTenantByKp("1", namespaceId);
            int configCount = persistService.configInfoCount(namespaceId);
            return new NamespaceAllInfo(namespaceId, tenantInfo.getTenantName(), 200,
                configCount, 2, tenantInfo.getTenantDesc());
        }
    }

    /**
     * create namespace
     *
     * @param request       request
     * @param response      response
     * @param namespaceName namespace Name
     * @param namespaceDesc namespace Desc
     * @return whether create ok
     */
    @PostMapping
    public Boolean createNamespace(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam("namespaceName") String namespaceName,
                                   @RequestParam(value = "namespaceDesc", required = false) String namespaceDesc) {
        // TODO 获取用kp
        String namespaceId = UUID.randomUUID().toString();
        persistService.insertTenantInfoAtomic("1", namespaceId, namespaceName, namespaceDesc, "nacos",
            System.currentTimeMillis());
        return true;
    }

    /**
     * edit namespace
     *
     * @param namespace         namespace
     * @param namespaceShowName namespace ShowName
     * @param namespaceDesc     namespace Desc
     * @return whether edit ok
     */
    @PutMapping
    public Boolean editNamespace(@RequestParam("namespace") String namespace,
                                 @RequestParam("namespaceShowName") String namespaceShowName,
                                 @RequestParam(value = "namespaceDesc", required = false) String namespaceDesc) {
        // TODO 获取用kp
        persistService.updateTenantNameAtomic("1", namespace, namespaceShowName, namespaceDesc);
        return true;
    }

    /**
     * del namespace by id
     *
     * @param request     request
     * @param response    response
     * @param namespaceId namespace Id
     * @return whether del ok
     */
    @DeleteMapping
    public Boolean deleteConfig(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam("namespaceId") String namespaceId) {
        persistService.removeTenantInfoAtomic("1", namespaceId);
        return true;
    }

}
