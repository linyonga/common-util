# common-util
工具

一 WikiUtils工具
  根据类对象自动生成wiki数据定义表格，只要将生成的数据粘贴到wiki中即可，不需要耗时得
  编辑wiki，一个一个添加字段。支持字段包括接口路径，参数和返回值描述，参数校验等信息。
  


二 IndexQueryParamUtils工具
   列表查询参数到检索参数的映射。查询和过滤字段到检索参数的拼接，通过注解方式动态生成，
   支持多关键字字段查询，多种过滤条件（如eq，in，范围查询等），避免繁琐的if和set操作，添加查询过滤字段只要添加注解。
 

三 增强copy工具
   二次开发开源copy工具
   支持copy对象
   A.a->B.a
   A.a->B.b
   A.a+A.b->B.a
   method(A.a)->B.a
