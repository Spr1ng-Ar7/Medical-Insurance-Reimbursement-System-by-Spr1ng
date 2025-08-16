package org.neu.insurance.util;

import org.neu.insurance.entity.Permission;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限树工具类
 */
public class PermissionTreeUtil {
    
    /**
     * 构建权限树
     * @param permissions 权限列表
     * @return 权限树根节点列表
     */
    public static List<Permission> buildTree(List<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 创建ID到权限的映射
        Map<Long, Permission> permissionMap = permissions.stream()
                .collect(Collectors.toMap(Permission::getId, permission -> permission));
        
        List<Permission> rootNodes = new ArrayList<>();
        
        for (Permission permission : permissions) {
            // 设置是否为叶子节点
            permission.setIsLeaf(true);
            
            if (permission.isRoot()) {
                // 根节点
                rootNodes.add(permission);
            } else {
                // 子节点，添加到父节点
                Permission parent = permissionMap.get(permission.getParentId());
                if (parent != null) {
                    parent.addChild(permission);
                    parent.setIsLeaf(false);
                }
            }
        }
        
        // 递归排序
        sortTree(rootNodes);
        
        return rootNodes;
    }
    
    /**
     * 递归排序权限树
     * @param nodes 节点列表
     */
    private static void sortTree(List<Permission> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return;
        }
        
        // 按排序字段排序
        nodes.sort((a, b) -> {
            int sortA = a.getSortOrder() != null ? a.getSortOrder() : 0;
            int sortB = b.getSortOrder() != null ? b.getSortOrder() : 0;
            return Integer.compare(sortA, sortB);
        });
        
        // 递归排序子节点
        for (Permission node : nodes) {
            if (node.hasChildren()) {
                sortTree(node.getChildren());
            }
        }
    }
    
    /**
     * 扁平化权限树
     * @param tree 权限树
     * @return 扁平化的权限列表
     */
    public static List<Permission> flattenTree(List<Permission> tree) {
        List<Permission> result = new ArrayList<>();
        flattenTreeRecursive(tree, result);
        return result;
    }
    
    /**
     * 递归扁平化权限树
     * @param nodes 节点列表
     * @param result 结果列表
     */
    private static void flattenTreeRecursive(List<Permission> nodes, List<Permission> result) {
        if (nodes == null || nodes.isEmpty()) {
            return;
        }
        
        for (Permission node : nodes) {
            result.add(node);
            if (node.hasChildren()) {
                flattenTreeRecursive(node.getChildren(), result);
            }
        }
    }
    
    /**
     * 查找权限节点
     * @param tree 权限树
     * @param permissionId 权限ID
     * @return 权限节点
     */
    public static Permission findNode(List<Permission> tree, Long permissionId) {
        if (tree == null || tree.isEmpty() || permissionId == null) {
            return null;
        }
        
        for (Permission node : tree) {
            if (permissionId.equals(node.getId())) {
                return node;
            }
            
            if (node.hasChildren()) {
                Permission found = findNode(node.getChildren(), permissionId);
                if (found != null) {
                    return found;
                }
            }
        }
        
        return null;
    }
    
    /**
     * 获取所有父节点ID
     * @param permissions 权限列表
     * @param permissionId 权限ID
     * @return 父节点ID列表
     */
    public static List<Long> getParentIds(List<Permission> permissions, Long permissionId) {
        List<Long> parentIds = new ArrayList<>();
        Map<Long, Permission> permissionMap = permissions.stream()
                .collect(Collectors.toMap(Permission::getId, permission -> permission));
        
        Permission current = permissionMap.get(permissionId);
        while (current != null && current.getParentId() != null && current.getParentId() != 0) {
            parentIds.add(current.getParentId());
            current = permissionMap.get(current.getParentId());
        }
        
        return parentIds;
    }
    
    /**
     * 获取所有子节点ID
     * @param tree 权限树
     * @param permissionId 权限ID
     * @return 子节点ID列表
     */
    public static List<Long> getChildIds(List<Permission> tree, Long permissionId) {
        List<Long> childIds = new ArrayList<>();
        Permission node = findNode(tree, permissionId);
        
        if (node != null && node.hasChildren()) {
            collectChildIds(node.getChildren(), childIds);
        }
        
        return childIds;
    }
    
    /**
     * 递归收集子节点ID
     * @param nodes 节点列表
     * @param childIds 子节点ID列表
     */
    private static void collectChildIds(List<Permission> nodes, List<Long> childIds) {
        for (Permission node : nodes) {
            childIds.add(node.getId());
            if (node.hasChildren()) {
                collectChildIds(node.getChildren(), childIds);
            }
        }
    }
    
    /**
     * 标记权限树中的选中状态
     * @param tree 权限树
     * @param selectedIds 选中的权限ID列表
     */
    public static void markSelected(List<Permission> tree, List<Long> selectedIds) {
        if (tree == null || tree.isEmpty() || selectedIds == null || selectedIds.isEmpty()) {
            return;
        }
        
        Set<Long> selectedIdSet = new HashSet<>(selectedIds);
        
        for (Permission node : tree) {
            markSelectedRecursive(node, selectedIdSet);
        }
    }
    
    /**
     * 递归标记选中状态
     * @param node 节点
     * @param selectedIds 选中的权限ID集合
     */
    private static void markSelectedRecursive(Permission node, Set<Long> selectedIds) {
        node.setIsSelected(selectedIds.contains(node.getId()));
        node.setIsChecked(selectedIds.contains(node.getId()));
        
        if (node.hasChildren()) {
            for (Permission child : node.getChildren()) {
                markSelectedRecursive(child, selectedIds);
            }
        }
    }
    
    /**
     * 获取权限树的深度
     * @param tree 权限树
     * @return 最大深度
     */
    public static int getTreeDepth(List<Permission> tree) {
        if (tree == null || tree.isEmpty()) {
            return 0;
        }
        
        int maxDepth = 0;
        for (Permission node : tree) {
            maxDepth = Math.max(maxDepth, getNodeDepth(node));
        }
        
        return maxDepth;
    }
    
    /**
     * 获取节点深度
     * @param node 节点
     * @return 节点深度
     */
    private static int getNodeDepth(Permission node) {
        if (node == null) {
            return 0;
        }
        
        if (!node.hasChildren()) {
            return 1;
        }
        
        int maxChildDepth = 0;
        for (Permission child : node.getChildren()) {
            maxChildDepth = Math.max(maxChildDepth, getNodeDepth(child));
        }
        
        return maxChildDepth + 1;
    }
    
    /**
     * 验证权限树结构
     * @param tree 权限树
     * @return 验证结果
     */
    public static boolean validateTree(List<Permission> tree) {
        if (tree == null) {
            return false;
        }
        
        Set<Long> visitedIds = new HashSet<>();
        
        for (Permission node : tree) {
            if (!validateNode(node, visitedIds)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 验证节点
     * @param node 节点
     * @param visitedIds 已访问的ID集合
     * @return 验证结果
     */
    private static boolean validateNode(Permission node, Set<Long> visitedIds) {
        if (node == null || node.getId() == null) {
            return false;
        }
        
        // 检查是否有循环引用
        if (visitedIds.contains(node.getId())) {
            return false;
        }
        
        visitedIds.add(node.getId());
        
        if (node.hasChildren()) {
            for (Permission child : node.getChildren()) {
                if (!validateNode(child, visitedIds)) {
                    return false;
                }
            }
        }
        
        return true;
    }
} 