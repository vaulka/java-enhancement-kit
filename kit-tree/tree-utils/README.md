# tree-utils 模块说明

> 树形结构 utils 模块

## 功能说明

* 构建树形结构。

## 使用

```java

import com.alibaba.fastjson.JSON;
import com.pongsky.kit.tree.entity.TreeNode;
import com.pongsky.kit.tree.utils.TreeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengsenhao
 */
public class TreeUtilsTest {

    public static class User extends TreeNode {

        public User(Long id, Long parentId, String name, Integer sex) {
            super(id.toString(), parentId.toString());
            this.name = name;
            this.sex = sex;
        }

        public User() {
        }

        private String name;

        private Integer sex;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getSex() {
            return sex;
        }

        public void setSex(Integer sex) {
            this.sex = sex;
        }


    }

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, 0L, "a", 1));
        users.add(new User(2L, 0L, "b", 2));
        users.add(new User(3L, 1L, "c", 3));
        users.add(new User(4L, 1L, "d", 4));
        users.add(new User(5L, 2L, "e", 5));
        users.add(new User(6L, 2L, "f", 6));
        users.add(new User(7L, 5L, "g", 7));
        users.add(new User(8L, 9L, "h", 8));
        List<User> treeNodes = new TreeUtils<User>().buildNode(users);
        System.out.println(JSON.toJSONString(treeNodes));
    }

}

```

效果如下：

```json

[
  {
    "children": [
      {
        "children": [],
        "id": "3",
        "name": "c",
        "parentId": "1",
        "sex": 3
      },
      {
        "children": [],
        "id": "4",
        "name": "d",
        "parentId": "1",
        "sex": 4
      }
    ],
    "id": "1",
    "name": "a",
    "parentId": "0",
    "sex": 1
  },
  {
    "children": [
      {
        "children": [
          {
            "children": [],
            "id": "7",
            "name": "g",
            "parentId": "5",
            "sex": 7
          }
        ],
        "id": "5",
        "name": "e",
        "parentId": "2",
        "sex": 5
      },
      {
        "children": [],
        "id": "6",
        "name": "f",
        "parentId": "2",
        "sex": 6
      }
    ],
    "id": "2",
    "name": "b",
    "parentId": "0",
    "sex": 2
  }
]

```