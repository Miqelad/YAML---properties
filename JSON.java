        /*Для удобства в json*/
        List<Structure> list = new ArrayList<>(structureRepository.listStructure());
        File file = new File(files);
        ObjectMapper mapper=new ObjectMapper(); // специальный класс от jackson для работы с json
        mapper.writeValue(file,list);
