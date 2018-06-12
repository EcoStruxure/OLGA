using System;
using Olga.Trinity.Exceptions;
using Semiodesk.Trinity;

namespace Olga.Trinity
{
    /// <summary>
    /// Custom model base class for any Resource subclass when access to a RdfContext instance is required at run time.  
    /// </summary>
    /// <remarks>
    /// While use of this base class is optional, it is strongly encouraged if Resource subclasses need to query it's underlying Model instance.  In these cases, using the Context
    /// instead will ensure a uniform experience for consumers of your model.
    /// </remarks>
    public class ContextResource : Resource
    {
        #region Constructor
        public ContextResource(Uri uri) : base(uri)
        {

        }
        #endregion

        #region Context
        /// <summary>
        /// Access to the RDF context
        /// </summary>
        public IRdfContext Context { get; set; }
        #endregion
        #region AssertContext
        protected IRdfContext AssertContext()
        {
            if (Context == null || Context.IsDisposed) throw new InvalidContextException();
            return Context;
        }
        #endregion
    }
}