package com.playground.playground;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;

public class Test {

    public static void main(String[] args) {
        List<MenuPO> menuList = new ArrayList<>();
        menuList.add(new MenuPO("1", "func1", null));
        menuList.add(new MenuPO("2", "func2", "1"));
        menuList.add(new MenuPO("3", "func3", "1"));
        menuList.add(new MenuPO("4", "func4", "2"));

        Set<String> funcIds = new HashSet<>();
        funcIds.add("func1");
        funcIds.add("func2");

        List<MenuResDTO> resultList = getAuthMenu(menuList, funcIds);

        for (MenuResDTO dto : resultList) {
            System.out.println(dto.getId() + " - " + dto.getName());
            for (MenuResDTO child : dto.getChildren()) {
                System.out.println("  - " + child.getId() + " - " + child.getName());
            }
        }
    }

    private static List<MenuResDTO> getAuthMenu(List<MenuPO> menuList, Set<String> funcIds) {
        Map<String, MenuResDTO> map = new HashMap<>();

        for (int i = menuList.size() - 1; i >= 0; i--) {
            MenuPO po = menuList.get(i);
            String id = po.getId();
            String funcId = po.getFuncId();
            String parent = po.getParent();

            if (funcIds.contains(funcId)) {
                MenuResDTO dto = new MenuResDTO();
                BeanUtils.copyProperties(po, dto);

                if (parent == null || parent.isEmpty()) {
                    MenuResDTO temp = new MenuResDTO();
                    map.put(id, temp);
                    BeanUtils.copyProperties(po, temp);
                } else {
                    if (!map.containsKey(parent)) {
                        MenuResDTO temp = new MenuResDTO();
                        map.put(parent, temp);
                    }
                    map.get(parent).getChildren().add(0, dto);
                }
            } else if (map.containsKey(id)) {
                MenuResDTO temp = map.get(id);
                BeanUtils.copyProperties(po, temp);
            }
        }

        List<MenuResDTO> resultList = new ArrayList<>();
        for (MenuPO po : menuList) {
            if (po.getParent() == null || po.getParent().isEmpty()) {
                MenuResDTO temp = map.get(po.getId());
                if (temp != null) {
                    resultList.add(temp);
                }
            }
        }

        return resultList;
    }

    public static class MenuPO {
        private String id;
        private String funcId;
        private String parent;

        public MenuPO(String id, String funcId, String parent) {
            this.id = id;
            this.funcId = funcId;
            this.parent = parent;
        }

        public String getId() {
            return id;
        }

        public String getFuncId() {
            return funcId;
        }

        public String getParent() {
            return parent;
        }
    }

    public static class MenuResDTO {
        private String id;
        private String name;
        private List<MenuResDTO> children = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<MenuResDTO> getChildren() {
            return children;
        }

        public void setChildren(List<MenuResDTO> children) {
            this.children = children;
        }
    }
}
