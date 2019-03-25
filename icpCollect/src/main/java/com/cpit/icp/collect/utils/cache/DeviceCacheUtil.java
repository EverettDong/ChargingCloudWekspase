package com.cpit.icp.collect.utils.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.cpit.icp.dto.collect.GateRouteInfoDto;
import com.cpit.icp.dto.collect.MonFaultRecordDto;

@Component
public class DeviceCacheUtil {
	@Cacheable(cacheNames= "collect-deviceGateWay", key="#root.caches[0].name+#deviceNo")
	public GateRouteInfoDto getGateRouteInfo(String deviceNo){
		return null;
	}
	
	@CachePut(cacheNames= "collect-deviceGateWay", key="#root.caches[0].name+#deviceNo")
	public GateRouteInfoDto upGateRouteInfo(String deviceNo,GateRouteInfoDto routeInfoDto){
		
		return routeInfoDto;
	}
	
	//<deviceNo,codeMode>
	@CachePut(cacheNames= "collect-deviceCodeMode", key="#root.caches[0].name+#deviceNo")
	public String setDeviceCodeMode(String deviceNo,String codeMode) {
		return codeMode;
	}
	@Cacheable(cacheNames= "collect-deviceCodeMode", key="#root.caches[0].name+#deviceNo")
	public String getDeviceCodeMode(String deviceNo) {
		return null;
	}
}
