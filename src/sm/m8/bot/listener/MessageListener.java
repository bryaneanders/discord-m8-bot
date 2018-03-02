package sm.m8.bot.listener;

import java.util.List;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import sm.m8.bot.constants.MessageConstants;

/**
 * 
 * 
 * @author Bryan Anders
 *
 */
public class MessageListener extends ListenerAdapter {
	
	public static final String M8_KEYWORD = "m8";
	public static final String CHANNEL_NAME = "general";
	private static final String TOKEN = "Mzg5NjA2MTg4MjQyNjk4Mjc0.DRhTew.ssbamz2EucwRF8PZLA-THlyG8-Y";
	
	private Logger logger = LoggerFactory.getLogger(MessageListener.class);

	public static void main(String[] args) throws LoginException, RateLimitedException, InterruptedException
    {
        JDA jda = new JDABuilder(AccountType.BOT)
        		.setToken(MessageListener.TOKEN)
        		.addEventListener(new MessageListener())
        		.buildAsync();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
    	// ignore messages from bots unless otherwise specified
    	if(event.getAuthor().isBot() && !respondToBots()) {
    		return;
    	}
    	
    	// check for the m8 keyword inside the message
    	if(!event.getMessage().getRawContent().contains(MessageListener.M8_KEYWORD)) {
    		return;
    	}
    	
    	// get the guild's text channels
    	Guild guild = event.getGuild();
    	List<TextChannel> textChannels =  guild.getTextChannelsByName(MessageListener.CHANNEL_NAME, true);
    	
    	// log and error and bail if no channels match 
    	if(textChannels != null && !textChannels.isEmpty()) {
    		if(logger.isErrorEnabled()) {
    			logger.error("No text channels found for channel name: " + MessageListener.CHANNEL_NAME);
    		}
    		return;
    	}
    	
    	// send the message asynchronously
    	//textChannels.get(0).sendMessage(MessageConstants.M8_MESSAGE).queue();

    	
    }

    public Message sendMessage(MessageReceivedEvent event, Message message) {
		if(event.isFromType(ChannelType.PRIVATE)) {
			//return event.getPrivateChannel().sendMessage(message).complete();
			return event.getPrivateChannel().sendMessage(message).queue();
		} else {
			//return event.getTextChannel().sendMessage(message).complete();
			return event.getTextChannel().sendMessage(message).queue();
		}
	}

}
