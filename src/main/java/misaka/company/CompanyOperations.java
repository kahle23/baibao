package misaka.company;

import artoria.lang.Operations;

/**
 * It cannot use generics, which would result in multiple method parameters having only one type.
 * It return value can be customized by subclasses, and it is used as a specification only.
 *
 * simple
 * info
 * search
 * members
 * stockholders
 *
 */
@Deprecated // TODO: 2023/3/27 Deletable
public interface CompanyOperations extends Operations {

}
