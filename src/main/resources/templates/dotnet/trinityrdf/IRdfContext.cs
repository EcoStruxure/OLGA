using System;
using System.Linq;
using System.Linq.Expressions;
using Semiodesk.Trinity;

namespace Olga.Trinity
{
    public interface IRdfContext 
    {
        #region Any
        /// <summary>
        /// Returns true if any <seealso cref="T"/> are found based on the optional expression supplied.
        /// </summary>
        /// <typeparam name="T">Type of entity to query for</typeparam>
        /// <param name="expression">Optional expression to refine the returned data before affecting the query.</param>
        /// <param name="reasoningMode">Controls how reasoning is applied when querying the RDF store.</param>
        bool Any<T>(Expression<Func<T, bool>> expression, ReasoningMode reasoningMode = ReasoningMode.AsNeeded)
            where T : Resource;
        #endregion
        #region Count
        /// <summary>
        /// Returns the count of <seealso cref="T"/> based on the optional expression supplied.
        /// </summary>
        /// <typeparam name="T">Type of entity to query for</typeparam>
        /// <param name="expression">Optional expression to refine the returned data before affecting the query.</param>
        /// <param name="reasoningMode">Controls how reasoning is applied when querying the RDF store.</param>
        int Count<T>(Expression<Func<T, bool>> expression = null, ReasoningMode reasoningMode = ReasoningMode.AsNeeded)
            where T : Resource;
        #endregion
        #region FirstOrDefault
        /// <summary>
        /// Returns an instance of <seealso cref="T"/> based on the optional expression supplied.
        /// </summary>
        /// <typeparam name="T">Type of entity to query for</typeparam>
        /// <param name="expression">Optional expression to refine the returned data before affecting the query.</param>
        /// <param name="reasoningMode">Controls how reasoning is applied when querying the RDF store.</param>
        T FirstOrDefault<T>(Expression<Func<T, bool>> expression, ReasoningMode reasoningMode = ReasoningMode.AsNeeded)
            where T : Resource;
        #endregion
        #region Where
        /// <summary>
        /// Returns an IEnumerable of <seealso cref="T"/> based on the optional expression supplied.
        /// </summary>
        /// <typeparam name="T">Type of entity to query for</typeparam>
        /// <param name="expression">Optional expression to refine the returned data before affecting the query.</param>
        /// <param name="reasoningMode">Controls how reasoning is applied when querying the RDF store.</param>
        IQueryable<T> Where<T>(Expression<Func<T, bool>> expression, ReasoningMode reasoningMode = ReasoningMode.AsNeeded)
            where T : Resource;
        #endregion

        /// <summary>
        /// Indicates that the context has been disposed
        /// </summary>
        bool IsDisposed { get; }
    }
}
