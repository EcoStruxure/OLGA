using System;

namespace Olga.Trinity.Attriubtes
{
    /// <summary>
    /// Model classes decorated with this class level attribute will be queried for with reasoning enabled
    /// </summary>
    [AttributeUsage(AttributeTargets.Class, AllowMultiple = false)]
    public class QueryWithReasoningAttribute : Attribute
    {
    }
}
