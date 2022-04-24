# spring-boot-starter-excel 模块说明

> Excel Spring Boot Starter 模块
>
> 该模块依赖
> * [excel-utils](../excel-utils/README.md) 模块

## 功能说明

* 将同一个接口支持导出功能。
* 支持标题值动态设置。

## 约定

1. 需要支持导出的接口添加 `ExcelExport` 注解。
2. 接口返回的数据，必须是 `List` 类型，支持多层级属性获取。
3. 标题值动态设置，必须是 `List` 类型，支持多层级属性获取。
4. 接口 `param` 请求参数需传 `isExcelExport` 参数。

## 使用

```java

public class ExcelExportUtilsTest {

    @ExcelExport(value = RelicBasicInfoVo.class)
    @GetMapping
    public PageResponse<RelicBasicInfoVo> query() {
        return relicBasicInfoService.query(query);
    }

}

```

## 强大的对手

* [EasyExcel](https://www.yuque.com/easyexcel/doc/easyexcel)
