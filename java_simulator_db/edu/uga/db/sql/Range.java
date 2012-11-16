package edu.uga.db.sql;

import java.util.*;

/**
 * Class defines a range object
 * @author Li Zhen
 * @version 0.1
 */
public class Range {
	String start;
	String end;
	List<String> values;
	
	public static String NAME = "(Aaron:Abel:Abraham:Adam:Adrian:Alva:Alex:Alexander:Alan:Albert:Alfred:Andrew:Andy:Angus:Anthony:Arthur:Austin:Ben:Benson:Bill:Bob:Brandon:Brant:Brent:Brian:Bruce:Carl:Cary:Caspar:Charles:Cheney:Chris:Christian:Christopher:Colin:Cosmo:Daniel:Dennis:Derek:Donald:Douglas:David:Denny:Edgar:Edward:Edwin:Elliott:Elvis:Eric:Evan:Francis:Frank:Franklin:Fred:Gabriel:Gaby:Garfield:Gary:Gavin:George:Gino:Glen:Glendon:Harrison:Hugo:Hunk:Howard:Henry:Ignativs:Ivan:Isaac:Jack:Jackson:Jacob:James:Jason:Jeffery:Jerome:Jerry:Jesse:Jim:Jimmy:Joe:John:Johnny:Joseph:Joshua:Justin:Keith:Ken:Kenneth:Kenny:Kevin:Lance:Larry:Laurent:Lawrence:Leander:Lee:Leo:Leonard:Leopold:Loren:Lori:Lorin:Luke:Marcus:Marcy:Mark:Marks:Mars:Martin:Matthew:Michael:Mike:Neil:Nicholas:Oliver:Oscar:Paul:Patrick:Peter:Philip:Phoebe:Quentin:Randall:Randolph:Randy:Reed:Rex:Richard:Richie:Robert:Robin:Robinson:Rock:Roger:Roy:Ryan:Sam:Sammy:Samuel:Scott:Sean:Shawn:Sidney:Simon:Solomon:Spark:Spencer:Spike:Stanley:Steven:Stuart:Terence:Terry:Timothy:Tommy:Tom:Thomas:Tony:Tyler:Van:Vern:Vernon:Vincent:Warren:Wesley:William:Abigail:Abby:Ada:Adelaide:Adeline:Alexandra:Ailsa:Aimee:Alice:Alina:Allison:Amanda::Amy:Amber:Anastasia:Andrea:Angela:Angelia:Angelina:Ann:Anne:Annie:Anita:Ariel:April:Ashley:Aviva:Barbara:Beata:Beatrice:Becky:Betty:Blanche:Bonnie:Brenda:Camille:Candice:Carina:Carmen:Carol:Caroline:Carry:Carrie:Cassandra:Cassie:Catherine:Cathy:Chelsea:Charlene:Charlotte:Cherry:Chris:Christina:Christine:Christy:Cindy:Claudia:Cloris:Connie:Constance:Cora:Corrine:Crystal:Daisy:Daphne:Darcy:Debbie:Deborah:Debra:Demi:Diana:Dolores:Donna:Doris:Edith:Editha:Elaine:Eleanor:Elizabeth:Ella:Ellen:Ellie:Emerald:Emily:Emma:Enid:Elsa:Erica:Estelle:Esther:Eudora:Eva:Eve:Fannie:Fiona:Frances:Frederica:Frieda:Gina:Gillian:Gladys:Gloria:Grace:Greta:Gwendolyn:Hannah:Helena:Hellen:Hebe:Heidi:Ingrid:Ishara:Irene:Iris:Ivy:Jacqueline:Jamie:Jane:Jane:Jean:Jessica:Jessie:Jennifer:Jenny:Jill:Joan:Joanna:Jocelyn:Josephine:Josie:Joy:Joyce:Judith:Judy:Julia:Juliana:Julie:June:Karen:Karida:Katherine:Kate:Kathy:Katrina:Kay:Kelly:Kitty:Lareina:Laura:Lena:Lydia:Lillian:Linda:Lisa:Liz:Lorraine:Louisa:Louise:Lucia:Lucy:Lucine:Lulu:Lynn:Maggie:Mamie:Manda:Mandy:Margaret:Mariah:Martha:Mary:Matilda:Maureen:Mavis:Maxine:May:Mayme:Megan:Melinda:Melissa:Melody:Mercedes:Meredith:Michelle:Milly:Miranda:Miriam:Miya:Molly:Monica:Nancy:Natalie:Natasha:Nicole:Nikita:Nina:Olina:Oprah:Pamela:Paula:Pauline:Pearl:Peggy:Philomena:Phoebe:Phyllis:Polly:Priscilla:Quentina:Rachel:Rebecca:Regina:Rita:Rose:Roxanne:Ruth:Sabrina:Sandra:Samantha:Sandy:Sarah:Selm:Selina:Serena:Sharon:Sheila:Shelley:Sherry:Shirley:Silvia:Sonia:Stacy:Stella:Stephanie:Sue:Sunny:Susan:Tamara:Tammy:Tess:Teresa:Tiffany:Tina:Tracy:Vanessa:Vicky:Victoria:Vivian:Wanda:Wendy:Winnie:Yolanda:Yvett:Yvonne:Zoey)";
	public static String YEAR = "(1990:1991:1992:1993:1994:1995:1996:1997:1998:1999:2000:2001:2002:2003:2004:2005:2006:2007:2008:2009:2010:2011:2012,2013:2014:2015)";
	public static String DEPT_ID = "(CSCI:AGRC:LANG:LING:BUSI:CHEM:PHYS:BIOL:BIOI:ARIT:MARI:STAT:MATH:CCRC:GEOG:GEOL:FOOD:JOUR:MAME:TEXT:ELEC:ENEG:MECH:ENGI:INFS:AERO:PLAN:FORE)";
	public static String SEMESTER = "(SA2006:FA2006:SP2007:SA2007:FA2007:SP2008:SA2008:FA2008:SP2009:SA2009:FA2009)";
	public static String COURSE_NAME = "(Course1:Course2:Course3:Course4:Course5:Course6:Course7:Course8:Course9:Course10:Course11:Course12:Course13:Course14:Course15:Course16:Course17:Course18:Course19:Course20)";
	
	/**
	 * Construct a range object
	 */
	public Range(){
		values = new ArrayList<String>();
	}
	
	/**
	 * Add discrete value to range object
	 * @param v		the discrete range value
	 */
	public void addValue(String v){
		values.add(v);
	}
	
	/**
	 * Set the start value of a continuous range object
	 * @param start		the start value
	 */
	public void setStart(String start){
		this.start = start;
	}
	
	/**
	 * Set the end value of a continuous range object
	 * @param end		the end value
	 */
	public void setEnd(String end){
		this.end = end;
	}
	
	/**
	 * Parse a specific constraint into several ranges
	 * Syntax of a constraint:
	 * 		Parentheses() indicates discrete values
	 * 			eg. (1:2:3) = {1, 2, 3}
	 * 		Brace[] indicates start to end range
	 * 			eg. [15:20] = [15,20]
	 * 		semicolon indicates a separation of different ranges
	 * 			eg. (1:2:3);[15:20] = {1,2,3}U[15,20]
	 * @param constraint	the constraint
	 * @return	list of ranges
	 */
	@SuppressWarnings("unchecked")
	public static List<Range> parseRange(String constraint){
		StringTokenizer token = new StringTokenizer(constraint, ";");
		List<Range> result = new ArrayList<Range>();
		while (token.hasMoreTokens()){
			String range = token.nextToken();
			if (range.charAt(0) == '('){
				// range of discrete values
				Range r = new Range();
				String[] values = range.substring(1, range.length()-1).split(":");
				for (int i=0;i<values.length;i++){
					r.addValue(values[i]);
				}
				result.add(r);
			}
			else{
				// range of continuous values
				Range r = new Range();
				String[] values = range.substring(1, range.length()-1).split(":");
				if (values.length < 2){
					// cast it to one value discrete range
					r.addValue(values[0]);
				}
				else{
					// ignore all intermediate values
					r.setStart(values[0]);
					r.setEnd(values[values.length-1]);
				}
				result.add(r);
			}
		}
		return result;
	}
	
	public boolean isDiscrete(){
		return values.size() != 0;
	}
	
	public List<String> values(){
		return this.values;
	}
	
	public String getEnd(){
		return this.end;
	}
	
	public String getStart(){
		return this.start;
	}
}
