Live templates let you insert frequently-used or custom code constructs into your source code file quickly, efficiently, and accurately. They contain predefined code fragments. For use that, open "Settings" from File menu on Andorid Studio than type "live template" into search bar. Set the values like i mention below.

fast code for everyone. Cheers!


#####logfunc
Log.i(TAG, "$METHOD_NAME$(" + $PARAM$ + ")");

METHOD_NAME: methodName()
PARAM: groovyScript("_1.collect { it }.join(' + \", \" + ') ", methodParameters())

------

#####log
Log.i(TAG, "$END$");

------

#####listener
if($LISTENER$ != null){
    $LISTENER$.$METHODNAME$($ARGS$);
}

LISTENER: variableOfType(Type) - onListener
METHODNAME: methodName() - method
ARGS: arg - arg

------

#####psf
public static final String INTENT_EXTRA_$suffix$ = "$key$";

------

#####tag
private static final String TAG = "$CLASS_NAME$";

CLASS_NAME: className()

------

#####unsupporttedexception
throw new UnsupportedOperationException("$METHOD_NAME$(" + $PARAM$ + ")");

METHOD_NAME: methodName()
PARAM: groovyScript("_1.collect { it }.join(' + \", \" + ') ", methodParameters())

