using System;

namespace Olga.Trinity.Exceptions
{
    /// <summary>
    /// Exception thrown when the underlying context has been disposed.  
    /// </summary>
    public class InvalidContextException : ApplicationException
    {
        #region Constructor
        public InvalidContextException() : base(Resources.Glossary.InvalidContextError)
        {
        }
        #endregion
    }
}
