        /*Для удобства в json*/
        List<Structure> list = new ArrayList<>(structureRepository.listStructure());
        File file = new File(files);
/*Jackson 3.0 меняет ситуацию, поскольку для работы требуется Java 8, и поэтому он может напрямую поддерживать функции.
Из-за этого parameter-namesи datatypesмодули объединяются jackson-databind и не нуждаются в регистрации;
datetimeмодуль ( JavaTimeModule) остается отдельным модулем из-за его размера и возможностей настройки.
Таким образом, вам нужно будет только отдельно добавить модуль "Дата/время Java 8" */
        ObjectMapper mapper=new ObjectMapper().registerModule(new JavaTimeModule()); // специальный класс от jackson для работы с json
        mapper.writeValue(file,list);
