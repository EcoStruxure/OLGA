using System;
using System.Collections.Generic;
using Olga.Trinity.Attriubtes;
using Semiodesk.Trinity;

namespace Olga.Trinity
{
    /// <summary>
    /// Internal utility class which uses reflection to determine if reasoning should be used when querying by type.
    /// </summary>
    internal class ModelTypeReasonerInspector
    {
        private volatile Dictionary<string, bool> _cachedTypeRequirements;
        private static readonly Type ReasoningAttributeType = typeof(QueryWithReasoningAttribute);
        private readonly object _dictionaryLock = new object();
        
        #region Singleton
        private static ModelTypeReasonerInspector _instance;
        private ModelTypeReasonerInspector()
        {
            _cachedTypeRequirements = new Dictionary<string, bool>();
        }
        public static ModelTypeReasonerInspector Current => _instance ?? (_instance = new ModelTypeReasonerInspector());
        #endregion

        #region IsReasoningRequired
        /// <summary>
        /// Returns true if reasoning is requested based on the mode and type.
        /// </summary>
        public bool IsReasoningRequired<T>(ReasoningMode reasoningMode)
            where T : Resource
        {
            return IsReasoningRequired(reasoningMode, typeof(T));
        }
        /// <summary>
        /// Returns true if reasoning is requested based on the mode and type.
        /// </summary>
        public bool IsReasoningRequired(ReasoningMode reasoningMode, Type type)
        {
            switch (reasoningMode)
            {
                case ReasoningMode.AsNeeded:
                    return InferReasoningFromType(type);
                case ReasoningMode.Always:
                    ;
                    return true;
                case ReasoningMode.Never:
                    return false;
                default:
                    throw new ArgumentOutOfRangeException(nameof(reasoningMode), reasoningMode, null);
            }
        }
        #endregion

        #region InferReasoningFromType
        private bool InferReasoningFromType(Type modelType)
        {
            var typeName = modelType.Name;
            lock (_dictionaryLock)
            {
                if (_cachedTypeRequirements.ContainsKey(typeName)) return _cachedTypeRequirements[typeName];

                var reasoningRequired = typeName == "Resource" || modelType.IsDefined(ReasoningAttributeType, false);
                _cachedTypeRequirements.Add(typeName, reasoningRequired);
                return reasoningRequired;
            }
        }
        #endregion
    }

    /// <summary>
    /// Controls how reasoning is applied to queries made against the RDF store
    /// </summary>
    public enum ReasoningMode
    {
        /// <summary>
        /// Reasoning will be used if the type of the query is decorated with <seealso cref="QueryWithReasoningAttribute"/>.
        /// </summary>
        AsNeeded,
        /// <summary>
        /// Reasoning will be used.
        /// </summary>
        Always,
        /// <summary>
        /// Reasoning will not be used.
        /// </summary>
        Never
    }

}
