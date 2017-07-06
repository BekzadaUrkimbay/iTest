package org.igov.service.business.action.task.systemtask.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.activiti.engine.delegate.DelegateExecution;
import org.igov.io.mail.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Elena отправка тела без атачмента - html text
 *
 */
@Component("MailTaskWithoutAttachment")
public class MailTaskWithoutAttachment extends Abstract_MailTaskCustom {

    private final static Logger LOG = LoggerFactory.getLogger(MailTaskWithoutAttachment.class);

    @Override
    public void execute(DelegateExecution oExecution) throws Exception {

        LOG.info("MailTaskWithoutAttachment listener started.");
        Map<String, Object> mExecutionVaraibles = oExecution.getVariables();
        LOG.info("mExecutionVaraibles={}", mExecutionVaraibles);
        if (!mExecutionVaraibles.isEmpty()) {
            Map<String, Object> mOnlyDateVariables = new HashMap<>();
            // выбираем все переменные типа Date, приводим к нужному формату 
            mExecutionVaraibles.forEach((sKey, oValue) -> {
                if (oValue != null) {
                    String soValue = oValue.toString();
                    String sClassName = oValue.getClass().getName();
                    if (sClassName.endsWith("Date")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, kk:mm", new Locale("uk", "UA"));
                        String sDate = sdf.format((Date) oValue);
                        mOnlyDateVariables.put(sKey, sDate);
                    } 
                }
            });
            //сетим отформатированные переменные в екзекьюшен
            oExecution.setVariables(mOnlyDateVariables);
        }
        Mail oMail = null;
        String sJsonMongo = loadFormPropertyFromTaskHTMLText(oExecution);
        LOG.info("sJsonMongo: {}", sJsonMongo);
        String sBodyFromMongoResult = getHtmlTextFromMongo(sJsonMongo);
        LOG.info("sBodyFromMongoResult: {}", sBodyFromMongoResult);
        if (sBodyFromMongoResult != null) {
            try {
                oMail = sendToMailFromMongo(oExecution);
            } catch (Exception ex) {
                LOG.error("MailTaskWithoutAttachment: ", ex);
            }
        } else {
            try {
                oMail = Mail_BaseFromTask(oExecution);
            } catch (Exception ex) {
                LOG.error("MailTaskWithoutAttachment: ", ex);
            }
        }

        sendMailOfTask(oMail, oExecution);
        LOG.info("MailTaskWithoutAttachment ok!");
    }

}
