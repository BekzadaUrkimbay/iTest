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
import java.util.Objects;
import java.util.Set;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.igov.model.core.BaseEntityDao;
import org.igov.model.subject.SubjectGroup;
import org.igov.model.subject.SubjectGroupResultTree;
import org.igov.model.subject.SubjectGroupTree;
import org.igov.model.subject.SubjectUser;
import org.igov.model.subject.VSubjectGroupParentNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * Сервис получения полной организационной иерархии (родитель - ребенок)
 *
 * @author inna
 */
@Service
public class SubjectGroupTreeService_new {

	private static final String ORGAN = "Organ";
	private static final String HUMAN = "Human";
	/**
	 * флаг определяющий, что на вход был конкрентный тип ORGAN или HUMAN
	 */
	private static boolean isSubjectType = false;
	private static final Log LOG = LogFactory.getLog(SubjectGroupTreeService_new.class);
	private static final long FAKE_ROOT_SUBJECT_ID = 0;

	@Autowired
	private BaseEntityDao<Long> baseEntityDao;

	@Autowired
	private IdentityService identityService;
	
	//Мапа для укладывания ид родителя и его детей в методе получения иерархии  getChildrenTree
    Map<Long, List<SubjectGroup>> getChildrenTreeRes = new HashMap<>();

	public SubjectGroupResultTree getCatalogSubjectGroupsTree(String sID_Group_Activiti, Long deepLevel,
			String sFind, Boolean bIncludeRoot,Long deepLevelWidth, String sSubjectType) {
		
		/**
		 * Лист для ид Subject ORGAN или HUMAN для последующего анализа
		 */
		List<Long> resSubjectTypeList = new ArrayList<>();
		List<SubjectGroup> aChildResult = new ArrayList<>();
		List<SubjectGroupTree> subjectGroupRelations = new ArrayList<>(baseEntityDao.findAll(SubjectGroupTree.class));
		 SubjectGroupResultTree processSubjectResultTree = new SubjectGroupResultTree();
		if(subjectGroupRelations!=null && !subjectGroupRelations.isEmpty()) {
		List<VSubjectGroupParentNode> parentSubjectGroups = new ArrayList<>();
		Map<Long, List<SubjectGroup>> subjToNodeMap = new HashMap<>();
		Map<SubjectGroup, List<SubjectGroup>> parentChildren = new HashMap<>();
		Map<String, Long> mapGroupActiviti = new HashMap<>();
		VSubjectGroupParentNode parentSubjectGroup = null;
		Set<Long> idParentList = new LinkedHashSet<>();
/*    	if(HUMAN.equals(sSubjectType)) {
    		isSubjectType = true;
    		subjectGroupRelations = Lists
                    .newArrayList(Collections2.filter(subjectGroupRelations, new Predicate<SubjectGroupTree>() {
                        @Override
                        public boolean apply(SubjectGroupTree subjectGroupTree) {
                            // получить только отфильтрованный
                            // список по Humans
                        	if(Objects.nonNull(subjectGroupTree.getoSubjectGroup_Parent().getoSubject())
                            		&& subjectGroupTree.getoSubjectGroup_Parent().getoSubject().getoSubjectHuman().getId()!=null) {
                        		
                        		resSubjectTypeList.add(subjectGroupTree.getoSubjectGroup_Parent().getoSubject().getId());
                        		
                        	}
                        	return Objects.nonNull(subjectGroupTree.getoSubjectGroup_Parent().getoSubject()) 
                            		&& Objects.nonNull(subjectGroupTree.getoSubjectGroup_Child().getoSubject())
                            		&& subjectGroupTree.getoSubjectGroup_Parent().getoSubject().getoSubjectHuman().getId()!=null
                            		&& subjectGroupTree.getoSubjectGroup_Child().getoSubject().getoSubjectHuman().getId()!=null;
                        }
                    }));
    		
		}
    	
    	if(ORGAN.equals(sSubjectType)) {
    		isSubjectType = true;
    		subjectGroupRelations = Lists
                    .newArrayList(Collections2.filter(subjectGroupRelations, new Predicate<SubjectGroupTree>() {
                        @Override
                        public boolean apply(SubjectGroupTree subjectGroupTree) {
                            // получить только отфильтрованный
                            // список по Humans
                        	if(Objects.nonNull(subjectGroupTree.getoSubjectGroup_Parent().getoSubject())
                            		&& subjectGroupTree.getoSubjectGroup_Parent().getoSubject().getoSubjectOrgan().getId()!=null) {
                        		
                        		resSubjectTypeList.add(subjectGroupTree.getoSubjectGroup_Parent().getoSubject().getId());
                        		
                        	}
                        	return Objects.nonNull(subjectGroupTree.getoSubjectGroup_Parent().getoSubject()) 
                            		&& Objects.nonNull(subjectGroupTree.getoSubjectGroup_Child().getoSubject())
                            		&& subjectGroupTree.getoSubjectGroup_Parent().getoSubject().getoSubjectOrgan().getId()!=null
                            		&& subjectGroupTree.getoSubjectGroup_Child().getoSubject().getoSubjectOrgan().getId()!=null;
                        }
                    }));
    		
		}*/
		for (SubjectGroupTree subjectGroupRelation : subjectGroupRelations) {
			
			final SubjectGroup parent = subjectGroupRelation.getoSubjectGroup_Parent();

			if (parent.getId() != FAKE_ROOT_SUBJECT_ID) {
				parentSubjectGroup = new VSubjectGroupParentNode();
				final SubjectGroup child = subjectGroupRelation.getoSubjectGroup_Child();
				if (!idParentList.contains(parent.getId())) {
					idParentList.add(parent.getId());
					// устанавливаем парентов
					parentSubjectGroup.setGroup(parent);
					// доавляем детей
					parentSubjectGroup.addChild(child);
					parentSubjectGroups.add(parentSubjectGroup);
					// мапа парент -ребенок
					subjToNodeMap.put(parent.getId(), parentSubjectGroup.getChildren());
					parentChildren.put(parent, parentSubjectGroup.getChildren());
					// мапа группа-ид парента
					mapGroupActiviti.put(parent.getsID_Group_Activiti(), parent.getId());
				} else {
					for (VSubjectGroupParentNode vSubjectGroupParentNode : parentSubjectGroups) {
						// убираем дубликаты
						if (vSubjectGroupParentNode.getGroup().getId().equals(parent.getId())) {
							// если дубликат парента-добавляем его детей к
							// общему списку
							vSubjectGroupParentNode.getChildren().add(child);
							// мапа парент-ребенок
							subjToNodeMap.put(parent.getId(), vSubjectGroupParentNode.getChildren());
							parentChildren.put(parent, parentSubjectGroup.getChildren());
							// мапа группа-ид парента
							mapGroupActiviti.put(parent.getsID_Group_Activiti(), parent.getId());
						}
					}
				}
			}

		}

		// Map<Long, List<SubjectGroup>> subjToNodeMapFiltr = new HashMap<>();
		// достаем ид sID_Group_Activiti которое на вход
		Long groupFiltr = mapGroupActiviti.get(sID_Group_Activiti);
		// детей его детей
		List<SubjectGroup> children = new ArrayList<>();

		List<Long> idChildren = new ArrayList<>();
		if (isDisplayRootElement(bIncludeRoot)) {
			SubjectGroup rootSubjectGroup = getRootSubjectGroup(parentChildren, groupFiltr);
			children.add(rootSubjectGroup);
		} else {
			// детей его детей
			children = subjToNodeMap.get(groupFiltr);
		}
		
		Map<Long, List<SubjectGroup>> hierarchyProcessSubject = new HashMap<>();
		// children полный список первого уровня
		if (children != null && !children.isEmpty()) {

			// получаем только ид чилдренов полного списка детей первого уровня
			idChildren = Lists
					.newArrayList(Collections2.transform(children, new Function<SubjectGroup, Long>() {
						@Override
						public Long apply(SubjectGroup subjectGroup) {
							return subjectGroup.getId();
						}
					}));
			aChildResult.addAll(children);
			hierarchyProcessSubject = getChildrenTree(children, idChildren, subjToNodeMap, idParentList, checkDeepLevel(deepLevel), 1, aChildResult);

		}

		 List<SubjectGroup> aChildResultByUser = filtrChildResultByUser(sFind, aChildResult);

	        List<SubjectGroup> resultTree = new ArrayList<>();
	        if (sFind != null && !sFind.isEmpty()) {
	        	resultTree = getSubjectGroupTree(hierarchyProcessSubject, aChildResultByUser);
	        	
	        }else {
	        	resultTree = getSubjectGroupTree(hierarchyProcessSubject, aChildResult);
	        }
	        if (isDisplayRootElement(bIncludeRoot)) {
			if (checkDeepLevelWidth(deepLevelWidth) < resultTree.size()) {
				if (resultTree != null && !resultTree.isEmpty()) {
					 List<SubjectGroup> result = new ArrayList<>();
					 result.add(resultTree.get(checkDeepLevelWidth(deepLevelWidth).intValue()));
					processSubjectResultTree.setaSubjectGroupTree(result);
				}
			}
	        }else {
	        	processSubjectResultTree.setaSubjectGroupTree(resultTree);
	        }
	        
	        /**
	         * isSubjectType =true- был на вход тип орган или хьман, 
	         * лист не пустой с ид Subject органа или хьманов,
	         * лист содержит groupFiltr
	         * возвращаем ответ, иначе ничего не возвращаем 
	         */
	        
	        if(isSubjectType && !resSubjectTypeList.isEmpty() && resSubjectTypeList.contains(groupFiltr)) {
	        	return processSubjectResultTree;
			}else {
				SubjectGroupResultTree processSubjectResultTreeRes = new SubjectGroupResultTree();
				 return processSubjectResultTreeRes;
			}
		}
		
	        return processSubjectResultTree;

	    }

//------------------------------------------------------------------------------Дополнительные методы-----------------------------------------------------------------
	
	 /**
     * Метод построения иерархии
     * @param hierarchySubjectGroup
     * @param aChildResult
     * @return List<SubjectGroup> - результирующий иерархический список
     */
    public List<SubjectGroup> getSubjectGroupTree(Map<Long, List<SubjectGroup>> hierarchySubjectGroup,
			List<SubjectGroup> aChildResult) {
		for (SubjectGroup subjectGroup : aChildResult) {
			subjectGroup.setaUser(getUsersByGroupSubject(subjectGroup.getsID_Group_Activiti()));
            //получаем по ключу лист детей и устанавливаем 
            List<SubjectGroup> aChildResultByKey = hierarchySubjectGroup.get(subjectGroup.getId());
            if (aChildResultByKey != null && !aChildResultByKey.isEmpty()) {
            	subjectGroup.setaSubjectGroup(aChildResultByKey);
            }
        }
		
		return aChildResult;
	}

    /**
     * Метод получения отфильтрованного списка объектов по заданному условию поиска
     * @param sFind - текст поиска в ФИО
     * @param aChildResult - результирующий лист, который фильтруем
     * @return List<SubjectGroup> - отфильтрованный список по строке поиска в фио
     */
	public List<SubjectGroup> filtrChildResultByUser(String sFind, List<SubjectGroup> aChildResult) {
		List<SubjectGroup> aChildResultByUser = new ArrayList<>();
        if (aChildResult != null && !aChildResult.isEmpty()) {
            if (sFind != null && !sFind.isEmpty()) {
                for (SubjectGroup subjectGroup : aChildResult) {
                    List<SubjectUser> aSubjectUser = getUsersByGroupSubject(
                    		subjectGroup.getsID_Group_Activiti());
                    final List<SubjectUser> subjectUserFiltr = Lists
                            .newArrayList(Collections2.filter(aSubjectUser, new Predicate<SubjectUser>() {
                                @Override
                                public boolean apply(SubjectUser subjectUser) {
                                    // получить только отфильтрованный
                                    // список по
                                    // sFind в фио
                                    return subjectUser.getsFirstName().toLowerCase().contains(sFind.toLowerCase());
                                }
                            }));
                    // получаем только их логины
                    final List<String> sFindLogin = Lists.newArrayList(
                            Collections2.transform(subjectUserFiltr, new Function<SubjectUser, String>() {
                                @Override
                                public String apply(SubjectUser subjectUser) {
                                    return subjectUser.getsLogin();
                                }
                            }));

                    // и оставляем только processSubject чьи логины
                    // содержаться
                    // в отфильтрованном списке
                    if (sFindLogin.contains(subjectGroup.getsID_Group_Activiti())) {
                        aChildResultByUser.add(subjectGroup);

                    }
                }
            }
        }
		return aChildResultByUser;
	}
	
	
	/**
	 * проверяем входящий параметр deepLevel
	 * @param deepLevel
	 * @return
	 */
	public Long checkDeepLevel(Long deepLevel) {
		if (deepLevel == null || deepLevel.intValue() == 0) {
			return 1000L;
		}
		return deepLevel;
	}
	

    /**
     * Метод структуру иерархии согласно заданной глубины и группы
     *
     * @param aChildLevel результирующий список со всеми нужными нам детьми
     * @param anID_ChildLevel ид детей уровня на котором мы находимся
     * @param subjToNodeMap мапа соответствия всех ид перентов и список его
     * детей
     * @param anID_PerentAll ид всех перентов
     * @param deepLevelRequested желаемая глубина
     * @param deepLevelFact фактическая глубина
     * @param result
     * @return Map<Long, List<ProcessSubject>>  - id-parent-->list child
     */
    
    public Map<Long, List<SubjectGroup>> getChildrenTree(List<SubjectGroup> aChildLevel, List<Long> anID_ChildLevel,
            Map<Long, List<SubjectGroup>> subjToNodeMap, Set<Long> anID_PerentAll, Long deepLevelRequested,
            int deepLevelFact, List<SubjectGroup> result) {
    	 List<SubjectGroup> aChildLevel_Result = new ArrayList<>();
        List<Long> anID_ChildLevel_Result = new ArrayList<>();
        if (deepLevelFact < deepLevelRequested.intValue()) {
            for (Long nID_ChildLevel : anID_ChildLevel) {
                if (anID_PerentAll.contains(nID_ChildLevel)) {
                    // достаем детей детей
                	aChildLevel_Result = subjToNodeMap.get(nID_ChildLevel);
                    if (aChildLevel_Result != null && !aChildLevel_Result.isEmpty()) {
                        // получаем только ид чилдренов
                        List<Long> anID_Child = Lists.newArrayList(
                                Collections2.transform(aChildLevel_Result, new Function<SubjectGroup, Long>() {
                                    @Override
                                    public Long apply(SubjectGroup subjectGroup) {
                                        return subjectGroup.getId();
                                    }
                                }));
                        //если anID_ChildLevel больше 1, то всех ид складываем в лист
                        anID_ChildLevel_Result.addAll(anID_Child);
                        // добавляем детей к общему списку детей
                        result.addAll(aChildLevel_Result);
                        getChildrenTreeRes.put(nID_ChildLevel, aChildLevel_Result);
                    }
                }
            }
            
            deepLevelFact++;
            if (deepLevelFact < deepLevelRequested.intValue()) {
                getChildrenTree(aChildLevel_Result, anID_ChildLevel_Result, subjToNodeMap, anID_PerentAll,
                        checkDeepLevel(deepLevelRequested), deepLevelFact, result);
            }
           
        }
        return getChildrenTreeRes;
    }

	/**
	 * Получение списка юзеров по ид группы 
	 * @param sID_Group_Activiti
	 * @return
	 */
	public List<SubjectUser> getUsersByGroupSubject(String sID_Group_Activiti) {

		List<SubjectUser> amsUsers = new ArrayList<>();
		List<User> aoUsers = sID_Group_Activiti != null
				? identityService.createUserQuery().memberOfGroup(sID_Group_Activiti).list()
				: identityService.createUserQuery().list();

		for (User oUser : aoUsers) {
			SubjectUser subjectUser = SubjectUser.BuilderHelper.buildSubjectUser(
					oUser.getId() == null ? "" : oUser.getId(),
					oUser.getFirstName() == null ? "" : oUser.getFirstName(),
					oUser.getLastName() == null ? "" : oUser.getLastName(),
					oUser.getEmail() == null ? "" : oUser.getEmail(), null);
			amsUsers.add(subjectUser);

		}

		return amsUsers;

	}

	/**
	 * Проверка флага на отображение рутового елемента:
	 * <p>
	 * <b>если null - устанавливать true для отображения по умолчанию</b>
	 *
	 * @param bIncludeRoot - флаг который прихоидит на вход (true - отображаем, false - нет)
	 * @return bIncludeRoot - фактическое значение флага
	 */
	public static boolean isDisplayRootElement(Boolean bIncludeRoot) {
		if (bIncludeRoot == null) {
			return Boolean.TRUE;
		}
		return bIncludeRoot;
	}
	
	
	/**
     * метод возвращающий значение deepLevelWidth
     * @param deepLevelWidth - ширина иерархии
     * @return deepLevelWidth - возвращается 1 (берем первый елемент из листа с объектами по иерархии) если на вход передали null или 0
     */
    public Long checkDeepLevelWidth(Long deepLevelWidth) {
        if (deepLevelWidth == null || deepLevelWidth.intValue() == 0 || deepLevelWidth.intValue()==1) {
            return 0L;
        }
        return deepLevelWidth-1;
    }

	/**
	 * Метод получения списка рутового елемента иерархии
	 * @param parentChildren - список парентов
	 * @param groupFiltr - ид, по которому строится иерархия
	 * @return ProcessSubject - рутовый елемент
	 */
	public SubjectGroup getRootSubjectGroup(Map<SubjectGroup, List<SubjectGroup>> parentChildren,
			Long groupFiltr) {

		SubjectGroup rootElement = null;
		for (Map.Entry<SubjectGroup, List<SubjectGroup>> entry : parentChildren.entrySet()) {
			rootElement = entry.getKey();
			if (rootElement.getId().equals(groupFiltr)) {
				return rootElement;
			}
		}
		return rootElement;
	}

}
