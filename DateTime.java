        /*Получаю дату с последним днем месяца*/
        LocalDate localDate = LocalDate.now().minusMonths(2).withDayOfMonth(LocalDate.now().minusMonths(2).lengthOfMonth());
