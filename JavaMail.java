@Service
public class EmailSenderServices {
    //    Класс SimpleMailMessage : используется для создания простого почтового
//    сообщения, включая поля
//    «от», «кому», «копия», «тема» и «текст».
    @Autowired
    private JavaMailSender mailSender;

    public  void  sendEmail(String[] toEmail,String subject,String body,String from,File file){
        /*Для отправки обычного сообщения*/
/*        SimpleMailMessage message= new SimpleMailMessage();
        //от
        message.setFrom(from);//от кого
        message.setTo(toEmail);//указываем несколько почт
        message.setText(body);//тело сообщения
        message.setSubject(subject);//тема
        //метод отправки самого сообщения
        mailSender.send(message);//запускает метод
        //вывод для runtime компилятора, что все отлично
        */
/*Для отправки сообщения в формате html*/
        try {

            MimeMessage message = mailSender.createMimeMessage();

            message.setSubject(subject);//тема
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);//от кого
            helper.setTo(toEmail);//указываем несколько почт
            helper.setText(body, true);//тело сообщения
            helper.addAttachment("JSON",file);

            mailSender.send(message);
        } catch (MessagingException ex) {
            System.err.println("ERROR MAIL SENDING");

        }

    }
}
