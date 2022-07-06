//nice link for guide  https://javarush.ru/groups/posts/2767-parsing-html-bibliotekoy-jsoup-
/*
        <dependency>
            <groupId>org.jsoup</groupId>
            <artifactId>jsoup</artifactId>
            <version>1.14.3</version>
        </dependency>
*/
/*https://jsoup.org/apidocs/*/

public void run(String... args) throws Exception {
//TODO: PARSER
        String  URL="https://ria.ru/";
            log.info("Start");
            Document document = Jsoup
                    .connect(URL)
                    .proxy("IP adress",8080)
                    .get();
            Elements body = document.select("a.cell-list__item-link.color-font-hover-only");
        log.info("Smart");
      // System.out.println(body);
            body.stream()
              //фильтруем по ключевому слову
                    .filter(e->e.text().toUpperCase().contains("word".toUpperCase()))
                    .forEach(a-> { System.out.println(a.attr("href") + " статья : " + a.text());
                        try {
                          //проваливаемся вглубь ссылки , чтобы достать там данные
                            Document documents = Jsoup
                                    .connect(a.attr("href"))
                                    .proxy("IP adress",8080)
                                    .get();
                            Elements bodys = documents.select("div.article__text");
                            bodys.stream().forEach(a1->System.out.println(a1.text()));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

    }
