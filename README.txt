Release Notes
 Revision 1.0
 
 New Features
 - Added ClassAccessor to access package private classes or private inner classes.
 - Added name mapping ability for ProxyAccessor
 
 Revision 0.9
 
 Fixes
 - FieldAccessor now searches the class hierarchy to find a specified field or method to access.
 
 New Features
 - Added StaticFieldAccessor to support static fields as well. 
 - Added ProxyAccessor that automagically create accessor to private fields using JavaBeans like naming conventions.
 - Deprecated and removed ProxyMethodAccessor, substituted by ProxyAccessor.
 - Extended the API for FieldAccessors to be able to reuse them over several instances of the same object type.
 
 Revision 0.7
 First release. 