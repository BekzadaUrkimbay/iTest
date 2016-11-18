/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.igov.service.business.subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.igov.model.core.BaseEntityDao;
import org.igov.model.subject.SubjectGroup;
import org.igov.model.subject.SubjectGroupTree;
import org.igov.model.subject.VSubjectGroupParentNode;
import org.igov.model.subject.VSubjectGroupTreeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * Сервис получения организационной иерархии
 *
 * @author inna
 */
@Service
public class SubjectGroupService {

    private static final Log LOG = LogFactory.getLog(SubjectGroupService.class);
    private static final long FAKE_ROOT_SUBJECT_ID = 0;
    public int i = 0;
    @Autowired
    private BaseEntityDao<Long> baseEntityDao;

    public List<List<SubjectGroup>> getCatalogTreeSubjectGroups(String sID_Group_Activiti, Long deepLevel) {
        List<SubjectGroupTree> subjectGroupRelations = new ArrayList<>(baseEntityDao.findAll(SubjectGroupTree.class));
        i = 0;
        List<VSubjectGroupParentNode> parentSubjectGroups = new ArrayList<>();
        Map<Long, List<SubjectGroup>> subjToNodeMap = new HashMap<>();
        Map<String, Long> mapGroupActiviti = new HashMap<>();
        VSubjectGroupParentNode parentSubjectGroup = null;
        Set<Long> idParentList = new LinkedHashSet<>();
        for (SubjectGroupTree subjectGroupRelation : subjectGroupRelations) {
            final SubjectGroup parent = subjectGroupRelation.getoSubjectGroup_Parent();

            if (parent.getId() != FAKE_ROOT_SUBJECT_ID) {
                parentSubjectGroup = new VSubjectGroupParentNode();
                final SubjectGroup child = subjectGroupRelation.getoSubjectGroup_Child();
                if (!idParentList.contains(parent.getId())) {
                    idParentList.add(parent.getId());
                    parentSubjectGroup.setGroup(parent); //устанавливаем парентов
                    parentSubjectGroup.addChild(child); //доавляем детей
                    parentSubjectGroups.add(parentSubjectGroup);
                    subjToNodeMap.put(parent.getId(), parentSubjectGroup.getChildren()); //мапа парент - ребенок
                    mapGroupActiviti.put(parent.getsID_Group_Activiti(), parent.getId()); // мапа группа - ид парента
                } else {
                    for (VSubjectGroupParentNode vSubjectGroupParentNode : parentSubjectGroups) {
                        //убираем дубликаты
                        if (vSubjectGroupParentNode.getGroup().getId().equals(parent.getId())) {
                            vSubjectGroupParentNode.getChildren().add(child); // если дубликат парента- добавляем его детей к общему списку
                            subjToNodeMap.put(parent.getId(), vSubjectGroupParentNode.getChildren());//мапа парент - ребенок
                            mapGroupActiviti.put(parent.getsID_Group_Activiti(), parent.getId());// мапа группа - ид парента
                        }
                    }
                }
            }

        }
        //subjToNodeMap - полный список ид и список всех детей.
        //mapGroupActiviti - полный список группа - ид парента

        Map<Long, List<SubjectGroup>> subjToNodeMapFiltr = new HashMap<>();
        List<List<SubjectGroup>> valuesRes = new ArrayList<>();
        Long groupFiltr = mapGroupActiviti.get(sID_Group_Activiti); //достаем ид sID_Group_Activiti которое на вход

        List<SubjectGroup> children = subjToNodeMap.get(groupFiltr); //достаем его детей
        // children полный список первого уровня
        if (children != null && !children.isEmpty()) {

            //получаем только ид чилдренов
            final List<Long> idChildren = Lists.newArrayList(
                    Collections2.transform(children, new Function<SubjectGroup, Long>() {
                        @Override
                        public Long apply(SubjectGroup subjectGroup) {
                            return subjectGroup.getId();
                        }
                    }));
            //idChildren ид полного списка детей первого уровня
            List<SubjectGroup> childrens = new ArrayList();
            getChildren(children, idChildren, subjToNodeMap, idParentList, deepLevel.intValue(), 0, childrens);

            subjToNodeMapFiltr.put(groupFiltr, childrens);
        }

        valuesRes = subjToNodeMapFiltr.values().stream().collect(Collectors.toList());

        VSubjectGroupTreeResult subjectGroupTreeResult = new VSubjectGroupTreeResult();
        parentSubjectGroup.accept(subjectGroupTreeResult);
        return valuesRes;

    }

    /**
     * Метод структуру иерархии согласно заданной глубины и группы
     *
     * @param aChildLevel результирующий список со всеми нужными нам детьми
     * @param anID_ChildLevel ид детей уровня на котором мы находимся
     * @param subjToNodeMap мапа соответствия всех ид перентов и список его
     * детей
     * @param anID_Perent ид всех перентов
     * @param deepLevelRequested желаемая глубина
     * @param deepLevelFact фактическая глубина
     * @param result
     * @return
     */
    public List<SubjectGroup> getChildren(List<SubjectGroup> aChildLevel, List<Long> anID_ChildLevel, Map<Long, List<SubjectGroup>> subjToNodeMap,
            Set<Long> anID_Perent, int deepLevelRequested, int deepLevelFact, List<SubjectGroup> result) {
        List<SubjectGroup> aChildLevel_Result = new ArrayList<>();
        List<Long> anID_ChildLevel_Result = new ArrayList<>();
        if (deepLevelRequested == 0) {
            deepLevelRequested = 1000;
        }
        if (deepLevelFact < deepLevelRequested) {
            for (Long nID_ChildLevel : anID_ChildLevel) {
                if (anID_Perent.contains(nID_ChildLevel)) {
                    aChildLevel_Result = subjToNodeMap.get(nID_ChildLevel);//достаем детей детей
                    if (aChildLevel_Result != null && !aChildLevel_Result.isEmpty()) {
                        //получаем только ид чилдренов
                        anID_ChildLevel_Result = Lists.newArrayList(
                                Collections2.transform(aChildLevel_Result, new Function<SubjectGroup, Long>() {
                                    @Override
                                    public Long apply(SubjectGroup subjectGroup) {
                                        return subjectGroup.getId();
                                    }
                                }));
                        result.addAll(aChildLevel_Result); //добавляем детей к общему списку детей
                    }
                }
            }
            deepLevelFact++;
            LOG.info("deepLevelFact: " + deepLevelFact + " deepLevelRequested: " + deepLevelRequested);
            if (deepLevelFact < deepLevelRequested) {
                getChildren(aChildLevel_Result, anID_ChildLevel_Result, subjToNodeMap, anID_Perent, deepLevelRequested, deepLevelFact, result);
            }
        }
        return result;
    }

    /**
     * Метод структуру иерархии согласно заданной глубины и группы
     *
     * @param childrens
     * @param idChildren
     * @param subjToNodeMap
     * @param idParentList
     * @param deepLevel
     * @return
     */
	public List<SubjectGroup> getChildrenNew(List<SubjectGroup> childrens,List<Long> idChildren , Map<Long, List<SubjectGroup>> subjToNodeMap,Set<Long> idParentList,int deepLevel){

		if(deepLevel==0) {
			deepLevel=1000;
        }
        i++;
		for(Long id : idChildren) {
			if (idParentList.contains(id)&& i<deepLevel) {
                List<SubjectGroup> child = subjToNodeMap.get(id);//достаем детей детей
				if(child!=null && !child.isEmpty()) {
                    //получаем только ид чилдренов
                    final List<Long> idCh = Lists.newArrayList(
                            Collections2.transform(child, new Function<SubjectGroup, Long>() {
                                @Override
                                public Long apply(SubjectGroup subjectGroup) {
                                    return subjectGroup.getId();
                                }
                            }));
					childrens.addAll(getChildrenNew(child,idCh,subjToNodeMap,idParentList,deepLevel)); //добавляем детей к общему списку детей
                }
            }
        }

        return childrens;

    }

}
