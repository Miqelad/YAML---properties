@Column(columnDefinition = "NVARCHAR(MAX)") - максимальное значение поля
@Table(name = "[Table]", schema = "dbo")
@Type(type="org.hibernate.type.StringNVarcharType") - работа с Nationalized типами, nchar-nvarchar в mssql  (cast (amess as nvarchar(40)))
Timestamp - sql время корректное делает из LocalDateTime
