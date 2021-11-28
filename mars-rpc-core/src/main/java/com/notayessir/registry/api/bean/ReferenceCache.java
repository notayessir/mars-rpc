package com.notayessir.registry.api.bean;

import java.util.*;

/**
 * 保存在服务消费者端的所有引用信息
 */
public class ReferenceCache {

    /**
     * 接口名 - 引用信息
     */
    private final Map<String, List<Reference>> referMap;

    public ReferenceCache() {
        referMap = new HashMap<>(64);
    }

    public Map<String, List<Reference>> getReferenceMap() {
        return referMap;
    }

    public List<Reference> getFirstReferInfoFromEachList(){
        List<Reference> references = new ArrayList<>(referMap.size());
        Collection<List<Reference>> referInfoListColl = referMap.values();
        for (List<Reference> referenceList : referInfoListColl){
            references.add(referenceList.get(0));
        }
        return references;
    }

    public List<String> getInterfaceNames(){
        List<String> interfaceNames = new ArrayList<>(referMap.size());
        Collection<List<Reference>> referInfoListColl = referMap.values();
        for (List<Reference> referenceList : referInfoListColl){
            interfaceNames.add(referenceList.get(0).getField().getType().getName());
        }
        return interfaceNames;
    }

    public void putReferences(List<Reference> references){
        for (Reference reference : references){
            String referName = reference.getField().getType().getName();
            if (referMap.containsKey(referName)){
                List<Reference> referenceList = referMap.get(referName);
                referenceList.add(reference);
                continue;
            }
            List<Reference> referenceList = new ArrayList<>();
            referenceList.add(reference);
            referMap.put(referName, referenceList);
        }
    }

    public boolean isEmpty(){
        return referMap.isEmpty();
    }


}
